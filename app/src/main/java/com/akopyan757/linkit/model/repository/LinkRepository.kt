package com.akopyan757.linkit.model.repository

import android.util.Log
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

    companion object {
        const val TAG = "LINK_REPOSITORY"
    }

    private val urlLinkDao: UrlLinkDao by mainInject()
    private val folderDao: FolderDao by mainInject()
    private val imageCache: ImageCache by mainInject()

    private val htmlParser: HtmlParser by mainInject()

    private val storeLinks: StoreLinks by mainInject()

    override val coroutineDispatcher: CoroutineDispatcher by inject(named(Config.IO_DISPATCHERS))

    fun initResources() = callIO {
        folderDao.initFolderDao()
        storeLinks.loadFolders().also { list ->
            folderDao.insertOrUpdate(list)
        }
        storeLinks.loadUrls().also { list ->
            list.forEach { data -> imageCache.saveImages(data) }
            urlLinkDao.insertOrUpdate(list)
        }
        Unit
    }

    fun addNewLink(urlLinkData: UrlLinkData) = callIO {
        if (FormatUtils.isUrl(urlLinkData.url)) {
            imageCache.saveImages(urlLinkData)
            val id = urlLinkDao.addNewData(urlLinkData)
            urlLinkData.id = id
            storeLinks.addLink(urlLinkData)
        }
    }

    fun getDefaultInfoFromUrl(url: String) = callIO {
        if (!FormatUtils.isUrl(url))
            throw UrlIsNotValidException()

        val htmlHeadTags = htmlParser.parseHeadTagsFromResource(url)
        return@callIO createNewUrlDataFromTags(htmlHeadTags)
    }

    fun addNewFolder(name: String) = callIO {
        val folder = folderDao.addNewFolder(name)
        if (folder != null) {
            storeLinks.addFolder(folder)
        } else throw FolderExistsException()
    }

    fun getUrlLinksByFolder(folderId: Int) = urlLinkDao.getLiveUrls(folderId)
            .map { list ->
                list.map { data ->
                    FormatUtils.extractUrls(data.title).forEach { path ->
                        data.title = data.title.replace(path, Config.EMPTY)
                    }
                    data.addFilePaths()
                }
            }
            .asLiveIO()

    fun getAllFolders() = folderDao.getLiveAll().asLiveIO()

    fun deleteUrls(ids: List<Long>) = callIO {
        urlLinkDao.removeByIds(ids)
        storeLinks.deleteUrls(ids)
    }

    fun deleteFolder(folderId: Int) = callIO {
        folderDao.removeById(folderId)
        storeLinks.deleteFolder(folderId)
    }

    fun renameFolder(folderId: Int, newFolderName: String) = callIO {
        folderDao.updateName(folderId, newFolderName)
    }

    fun moveLinkToTop(linkId: Long) = launchIO {
        urlLinkDao.moveUrlToTop(linkId)
    }

    fun reorderFolders(orders: List<Pair<Int, Int>>) = callIO {
        val pairs = folderDao.updateOrders(orders)
        storeLinks.reorderFolders(pairs)
    }

    fun moveScreenshotToImageFolder(linkId: Long) = callIO {
        imageCache.moveScreenshot(linkId)
    }

    /** Private methods **/
    private fun UrlLinkData.addFilePaths() = apply {
        logoFileName = imageCache.getLogoName(this)
        contentFileName = imageCache.getContentName(this)
        screenshotFileName = imageCache.getScreenshotName(this.id)
    }

    private fun createNewUrlDataFromTags(htmlTags: HtmlTags): UrlLinkData {
        return UrlLinkData().apply {
            title = htmlTags.getTitle()
            description = htmlTags.getDescription()
            photoUrl = htmlTags.getImage()
        }
    }
}