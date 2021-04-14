package com.akopyan757.linkit_domain.repository

import com.akopyan757.linkit_domain.entity.FolderEntity
import io.reactivex.Observable

interface ILocalFolderDataSource {
    fun createFolder(folderName: String): FolderEntity
    fun checkExistFolder(folderId: String): Boolean
    fun getAllFolders(): List<FolderEntity>
    fun updateAllFolders(folders: List<FolderEntity>)
    fun updateFolder(folder: FolderEntity)
    fun removeFolderById(folderId: String)
    fun listenFolders(): Observable<List<FolderEntity>>
}