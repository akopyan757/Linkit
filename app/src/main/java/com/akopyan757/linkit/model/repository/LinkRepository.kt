package com.akopyan757.linkit.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.akopyan757.base.model.BaseRepository
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.utils.FormatUtils
import com.akopyan757.linkit.model.cache.ImageCache
import com.akopyan757.linkit.model.database.FolderDao
import com.akopyan757.linkit.model.database.UrlLinkDao
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.model.exception.FolderExistsException
import com.akopyan757.linkit.model.exception.UrlIsNotValidException
import com.akopyan757.linkit.model.parser.HtmlParser
import com.akopyan757.linkit.model.parser.tags.HtmlTags
import com.akopyan757.linkit.model.store.StoreLinks
import com.akopyan757.linkit.view.scope.mainInject
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

class LinkRepository: BaseRepository(), KoinComponent {

    private val urlLinkDao: UrlLinkDao by mainInject()
    private val folderDao: FolderDao by mainInject()
    private val imageCache: ImageCache by mainInject()
    private val htmlParser: HtmlParser by mainInject()
    private val storeLinks: StoreLinks by mainInject()

    override val coroutineDispatcher: CoroutineDispatcher by inject(named(Config.IO_DISPATCHERS))

    fun fetchRemoteData() = wrapActionIO {
        folderDao.initFolderDao()
        val storeFolders = storeLinks.loadFolders()
        val storeUrls = storeLinks.loadUrls()
        folderDao.insertOrUpdate(storeFolders)
        storeUrls.forEach { data -> imageCache.saveImages(data) }
        urlLinkDao.insertOrUpdate(storeUrls)
    }

    fun addNewLink(url: String, folderId: Int) = wrapActionIO {
        if (!FormatUtils.isUrl(url))
            throw UrlIsNotValidException()
        val data = UrlLinkData.createWithAssignFolder(url, folderId)
        val linkId = urlLinkDao.addNewData(data)
        data.updateId(linkId)
        storeLinks.addLink(data)
        loadExtraDataForUrlData(data)
    }

    fun addNewFolder(name: String) = wrapActionIO {
        val folder = folderDao.addNewFolder(name)
        if (folder != null) {
            storeLinks.addFolder(folder)
        } else {
            throw FolderExistsException()
        }
    }

    fun getUrlLinksByFolder(folderId: Int): LiveData<List<UrlLinkData>> {
        return urlLinkDao.getLiveUrls(folderId)
            .map { urlsData -> editUrlDataList(urlsData) }
            .asLiveIO()
    }

    fun getAllFolders() = folderDao.getLiveAll().asLiveIO()

    fun deleteUrls(ids: List<Long>) = wrapActionIO {
        urlLinkDao.removeByIds(ids)
        storeLinks.deleteUrls(ids)
    }

    fun deleteFolder(folderId: Int) = wrapActionIO {
        folderDao.removeById(folderId)
        storeLinks.deleteFolder(folderId)
    }

    fun renameFolder(folderId: Int, newFolderName: String) = wrapActionIO {
        folderDao.updateName(folderId, newFolderName)
    }

    fun moveLinkToTop(linkId: Long) = launchIO {
        urlLinkDao.moveUrlToTop(linkId)
    }

    fun reorderFolders(orders: List<Pair<Int, Int>>) = wrapActionIO {
        val pairs = folderDao.updateOrders(orders)
        storeLinks.reorderFolders(pairs)
    }

    fun moveScreenshotToImageFolder(linkId: Long) = wrapActionIO {
        imageCache.moveScreenshot(linkId)
    }

    private fun loadExtraDataForUrlData(data: UrlLinkData) = launchIO {
        val htmlHeadTags = htmlParser.parseHeadTagsFromResource(data.url)
        val urlLinkData = data.fillUrlDataWithHtmlTags(htmlHeadTags)
                .editFields()
                .addMediaFilenames()
        urlLinkDao.insertOrUpdate(urlLinkData)
        storeLinks.addLink(urlLinkData)
        imageCache.saveImages(urlLinkData)
    }

    private fun UrlLinkData.fillUrlDataWithHtmlTags(htmlTags: HtmlTags): UrlLinkData {
        title = htmlTags.getTitle()
        description = htmlTags.getDescription()
        photoUrl = htmlTags.getImage()
        return this
    }

    private fun editUrlDataList(urlDataList: List<UrlLinkData>): List<UrlLinkData> {
        return urlDataList.map { urlData ->
            urlData.editFields()
                   .addMediaFilenames()
        }
    }

    private fun UrlLinkData.editFields(): UrlLinkData {
        title = removeUrlsFromText(title)
        description = removeUrlsFromText(description)
        return this
    }

    private fun UrlLinkData.addMediaFilenames(): UrlLinkData {
        logoFileName = imageCache.getLogoName(this)
        contentFileName = imageCache.getContentName(this)
        screenshotFileName = imageCache.getScreenshotName(this.id)
        return this
    }

    private fun removeUrlsFromText(text: String): String {
        var newText = text
        val extractedUrls = FormatUtils.extractUrls(text)
        extractedUrls.forEach { url ->
            newText = newText.replace(url, Config.EMPTY)
        }
        return newText
    }
}