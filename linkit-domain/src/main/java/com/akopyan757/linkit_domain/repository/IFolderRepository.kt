package com.akopyan757.linkit_domain.repository

interface IFolderRepository {
    fun createFolder(name: String)
    fun deleteFolder(folderId: String)
    fun renameFolder(folderId: String, name: String)
    fun reorderFolders(folderIds: List<String>)
}