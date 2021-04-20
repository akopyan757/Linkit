package com.akopyan757.linkit_domain.repository

import com.akopyan757.linkit_domain.entity.FolderEntity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface ILocalFolderDataSource {
    fun createFolderInstance(folderName: String): FolderEntity
    fun insertFolder(folder: FolderEntity): Completable
    fun checkExistFolder(folderId: String): Boolean
    fun getAllFolders(): List<FolderEntity>
    fun updateAllFolders(folders: List<FolderEntity>): Completable
    fun updateFolder(folder: FolderEntity)
    fun updateFolderName(folderId: String, folderName: String): Completable
    fun updateFoldersOrder(folderIds: List<String>): Completable
    fun removeFolderById(folderId: String)
    fun listenFolders(): Observable<List<FolderEntity>>
}