package com.akopyan757.linkit_model.datasource

import com.akopyan757.linkit_domain.entity.FolderEntity
import com.akopyan757.linkit_domain.repository.ILocalFolderDataSource
import com.akopyan757.linkit_model.database.FolderDao
import com.akopyan757.linkit_model.database.data.FolderData
import com.akopyan757.linkit_model.mapper.Mapper
import com.akopyan757.linkit_model.throwable.FolderExistsException
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.*

class LocalFolderDataSource(
    private val folderDao: FolderDao,
    private val mapper: Mapper<FolderData, FolderEntity>
): ILocalFolderDataSource {

    override fun createFolderInstance(folderName: String): FolderEntity {
        val existFolder = folderDao.getByName(folderName)
        val folderId = existFolder?.id ?: throw FolderExistsException()
        val order = folderDao.getMaxOrder().plus(ONE)
        return FolderEntity(folderId, folderName, order)
    }

    override fun insertFolder(folder: FolderEntity) = Completable.fromCallable {
        folderDao.insertOrUpdate(folder.let(mapper::secondToFirst))
    }

    override fun checkExistFolder(folderId: String): Boolean {
        return folderDao.checkFolderExist(folderId)
    }

    override fun getAllFolders(): List<FolderEntity> {
        return folderDao.getAll().map(mapper::firstToSecond)
    }

    override fun listenFolders(): Observable<List<FolderEntity>> {
        return folderDao.getAllLive().map { folderDataList ->
            folderDataList.map(mapper::firstToSecond)
        }.toObservable()
    }

    override fun updateAllFolders(folders: List<FolderEntity>) = Completable.fromCallable {
        val entities = folders.map(mapper::secondToFirst)
        folderDao.updateAll(entities)
    }

    override fun updateFolder(folder: FolderEntity) {
        val entities = folder.let(mapper::secondToFirst)
        folderDao.insertOrUpdate(entities)
    }

    override fun updateFolderName(folderId: String, folderName: String) = Completable.fromCallable {
        folderDao.updateFolderName(folderId, folderName)
    }

    override fun updateFoldersOrder(folderIds: List<String>) = Completable.fromAction {
        folderDao.updateFoldersOrder(folderIds)
    }

    override fun removeFolderById(folderId: String) {
        folderDao.removeById(folderId)
    }

    companion object {
        private const val ONE = 1
    }
}