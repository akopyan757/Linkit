package com.akopyan757.linkit.model.repository

import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.akopyan757.base.model.BaseRepository
import com.akopyan757.linkit.BuildConfig
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.utils.FormatUtils
import com.akopyan757.linkit.model.cache.ImageCache
import com.akopyan757.linkit.model.database.FolderDao
import com.akopyan757.linkit.model.database.PatternDao
import com.akopyan757.linkit.model.database.UrlLinkDao
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.model.exception.FolderExistsException
import com.akopyan757.linkit.model.exception.UrlIsNotValidException
import com.akopyan757.linkit.model.store.StoreLinks
import com.akopyan757.linkit.model.store.StorePatterns
import com.akopyan757.urlparser.IUrlParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

class LinkRepository: BaseRepository(), KoinComponent {

    companion object {
        const val TAG = "LINK_REPOSITORY"
    }

    private val urlLinkDao: UrlLinkDao by inject()
    private val folderDao: FolderDao by inject()
    private val patternDao: PatternDao by inject()
    private val imageCache: ImageCache by inject()

    private val urlParser: IUrlParser<UrlLinkData> by inject()

    private val storePatterns: StorePatterns by inject()
    private val storeLinks: StoreLinks by inject()

    override val coroutineDispatcher: CoroutineDispatcher by inject(named(Config.IO_DISPATCHERS))

    fun initResources() = callIO {
        folderDao.initFolderDao()
        storePatterns.fetchData()
        storeLinks.loadFolders().also { list ->
            folderDao.insertOrUpdate(list)
        }
        Unit
    }

    fun getLivePattern() = storePatterns.getLivePatterns().switchMap { items ->
        liveData(Dispatchers.IO) {
            items.forEach { item ->
                patternDao.addPatternWithHost(item)
                storePatterns.removeObserveItem(item)
                if (BuildConfig.DEBUG) {
                    urlLinkDao.getByHost(item.host).forEach { data ->
                        val newData = urlParser.parseUrl(data.url).also {
                            it.id = data.id
                        }
                        urlLinkDao.insertOrUpdate(newData)
                        imageCache.saveImages(newData)
                    }
                }
            }
            emit(Unit)
        }
    }

    fun addNewLink(urlLinkData: UrlLinkData) = callIO {
        if (FormatUtils.isUrl(urlLinkData.url)) {
            imageCache.saveImages(urlLinkData)
            urlLinkDao.addNewData(urlLinkData)
        }
    }

    fun getDefaultInfoFromUrl(url: String) = callIO {
        if (FormatUtils.isUrl(url)) urlParser.parseUrl(url) else throw UrlIsNotValidException()
    }

    fun addNewFolder(name: String) = callIO {
        val folder = folderDao.addNewFolder(name)
        if (folder != null) {
            storeLinks.addData(folder)
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

    fun getUrlLinksByFolder2(folderId: Int) = callIO {
        urlLinkDao.getByFolder(folderId).map { item -> item.addFilePaths() }
    }

    fun getAllFolders() = folderDao.getLiveAll().asLiveIO()

    fun deleteUrls(ids: List<Long>) = callIO {
        urlLinkDao.removeByIds(ids)
    }

    fun deleteFolder(folderId: Int) = callIO {
        folderDao.removeById(folderId)
        storeLinks.deleteFolder(folderId)
    }

    fun renameFolder(folderId: Int, newFolderName: String) = callIO {
        folderDao.updateName(folderId, newFolderName)
    }

    fun reorderLinks(orders: List<Long>) = callIO {
        urlLinkDao.updateOrders(orders)
    }

    fun reorderFolders(orders: List<Pair<Int, Int>>) = callIO {
        folderDao.updateOrders(orders)
    }

    private fun UrlLinkData.addFilePaths() = apply {
        logoFileName = imageCache.getLogoName(this)
        contentFileName = imageCache.getContentName(this)
    }
}