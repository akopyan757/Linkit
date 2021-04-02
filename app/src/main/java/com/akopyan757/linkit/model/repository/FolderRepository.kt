package com.akopyan757.linkit.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.akopyan757.base.model.BaseRepository
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.database.FolderDao
import com.akopyan757.linkit.model.entity.DataChange
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.source.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import java.util.*

class FolderRepository: BaseRepository(), KoinComponent {

    private val folderDao: FolderDao by inject()
    private val remoteDataSource: RemoteDataSource by inject()

    override val coroutineDispatcher: CoroutineDispatcher by inject(named(Config.IO_DISPATCHERS))

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

    fun reorderFolders(sortedFoldersIds: List<String>) = wrapActionIO {
        remoteDataSource.reorderFolders(sortedFoldersIds)
    }

    fun getFoldersFromCache() = wrapActionIOWithResult {
        folderDao.getAll()
    }

    fun listenFolderFromCache(): LiveData<List<FolderData>> {
        return folderDao.getAllLive().asLiveIO()
    }

    fun listenRemoteData(): LiveData<Unit> = liveData {
        remoteDataSource.loadFolders().also { firstData ->
            saveChangesToCache(DataChange.Initialized(firstData))
            emit(Unit)
        }

        val liveData = remoteDataSource.listenFolderChanges().map { dataChange ->
            runBlocking(coroutineDispatcher) {
                saveChangesToCache(dataChange)
            }
        }
        emitSource(liveData)
    }

    private fun saveChangesToCache(folderDataChange: DataChange<FolderData>) {
        when (folderDataChange) {
            is DataChange.Initialized -> folderDao.updateAll(folderDataChange.data)
            is DataChange.Added -> folderDao.insertOrUpdate(folderDataChange.data)
            is DataChange.Modified -> folderDao.insertOrUpdate(folderDataChange.data)
            is DataChange.Deleted -> folderDao.removeById(folderDataChange.data.id)
            else -> {}
        }
    }
}