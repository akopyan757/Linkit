package com.akopyan757.linkit.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.akopyan757.base.model.BaseRepository
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.utils.FormatUtils
import com.akopyan757.linkit.model.database.FolderDao
import com.akopyan757.linkit.model.database.UrlLinkDao
import com.akopyan757.linkit.model.entity.DataChange
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.model.exception.UrlIsNotValidException
import com.akopyan757.linkit.model.parser.HtmlParser
import com.akopyan757.linkit.model.source.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import java.util.*

class LinkRepository: BaseRepository(), KoinComponent {

    private val urlLinkDao: UrlLinkDao by inject()
    private val folderDao: FolderDao by inject()
    private val htmlParser: HtmlParser by inject()
    private val remoteDataSource: RemoteDataSource by inject()

    override val coroutineDispatcher: CoroutineDispatcher by inject(named(Config.IO_DISPATCHERS))

    fun createLink(url: String, folderId: String?) = wrapActionIO {
        if (!FormatUtils.isUrl(url))
            throw UrlIsNotValidException()
        val id = UUID.randomUUID().toString()
        val data = UrlLinkData(id, url, folderId=folderId)
        remoteDataSource.createOrUpdateUrlLink(data)
        loadExtraDataForUrlData(data)
    }

    fun createFolder(name: String) = wrapActionIO {
        val folderId = folderDao.getByName(name)?.id ?: UUID.randomUUID().toString()
        val order = folderDao.getMaxOrder().plus(1)
        val folderData = FolderData(folderId, name, order)
        remoteDataSource.createOrUpdateFolder(folderData)
    }

    fun deleteFolder(id: String) = wrapActionIO {
        remoteDataSource.deleteFolder(id)
    }

    fun renameFolder(folderId: String, newFolderName: String) = wrapActionIO {
        folderDao.getById(folderId) ?: throw Exception("Folder not found")
        remoteDataSource.setNameForFolder(folderId, newFolderName)
    }

    fun moveLinkToTop(urlLinkId: String) = launchIO {
        urlLinkDao.getById(urlLinkId) ?: throw Exception("Url link not found")
        val newOrder = urlLinkDao.getMaxOrder().plus(1)
        remoteDataSource.setOrderForUrlLink(urlLinkId, newOrder)
    }

    fun reorderFolders(sortedFoldersIds: List<String>) = wrapActionIO {
        remoteDataSource.reorderFolders(sortedFoldersIds)
    }

    fun getFoldersFromCache() = wrapActionIOWithResult {
        folderDao.getAll()
    }

    fun listenFolderFromCache() = folderDao.getAllLive().asLiveIO()

    fun listenUrlDataFromCache(folderId: String?): LiveData<List<UrlLinkData>> {
        return if (folderId == null) {
            urlLinkDao.getAllLive()
        } else {
            urlLinkDao.getByFolderLive(folderId)
        }.asLiveIO()
    }

    fun listenRemoteData(): LiveData<Unit> {
        val unionLive = MediatorLiveData<Unit>()
        unionLive.addSource(remoteDataSource.listenFolderChanges()) { dataChange ->
            runBlocking(coroutineDispatcher) {
                saveFolderChangesToCache(dataChange)
            }
            unionLive.value = Unit
        }
        unionLive.addSource(remoteDataSource.listenUrlLinksChanges()) { dataChange ->
            runBlocking(coroutineDispatcher) {
                saveUrlLinksChangesToCache(dataChange)
            }
            unionLive.value = Unit
        }
        return unionLive
    }

    private fun saveFolderChangesToCache(folderDataChange: DataChange<FolderData>) {
        when (folderDataChange) {
            is DataChange.Added -> folderDao.insertOrUpdate(folderDataChange.data)
            is DataChange.Modified -> folderDao.insertOrUpdate(folderDataChange.data)
            is DataChange.Deleted -> folderDao.removeById(folderDataChange.data.id)
            else -> {}
        }
    }

    private fun saveUrlLinksChangesToCache(urlLinkDataChange: DataChange<UrlLinkData>) {
        when (urlLinkDataChange) {
            is DataChange.Added -> urlLinkDao.insertOrUpdate(urlLinkDataChange.data)
            is DataChange.Modified -> urlLinkDao.insertOrUpdate(urlLinkDataChange.data)
            is DataChange.Deleted -> urlLinkDao.removeById(urlLinkDataChange.data.id)
            else -> {}
        }
    }

    private fun loadExtraDataForUrlData(data: UrlLinkData) = launchIO {
        val htmlHeadTags = htmlParser.parseHeadTagsFromResource(data.url)
        data.title = FormatUtils.highlightWithoutLink(htmlHeadTags.getTitle())
        data.description = FormatUtils.highlightWithoutLink(htmlHeadTags.getDescription())
        data.photoUrl = htmlHeadTags.getImage()
        remoteDataSource.createOrUpdateUrlLink(data)
    }
}