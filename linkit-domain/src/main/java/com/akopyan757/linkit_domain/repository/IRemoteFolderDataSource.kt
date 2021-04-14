package com.akopyan757.linkit_domain.repository

import com.akopyan757.linkit_domain.entity.DataChange
import com.akopyan757.linkit_domain.entity.FolderEntity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface IRemoteFolderDataSource {
    fun loadFolders(): Single<List<FolderEntity>>
    fun createOrUpdateFolder(data: FolderEntity): Completable
    fun deleteFolder(folderId: String): Completable
    fun setFolderName(folderId: String, folderName: String): Completable
    fun reorderFolders(folderIds: List<String>): Completable
    fun listenFolderChanges(): Observable<DataChange<FolderEntity>>
}