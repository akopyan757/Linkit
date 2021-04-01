package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.repository.LinkRepository

import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import org.koin.core.KoinComponent
import org.koin.core.inject

class FolderViewModel : BaseViewModel(), KoinComponent {

    private val linkRepository: LinkRepository by inject()

    @get:Bindable var newFolderName: String by DB("", BR.newFolderName)

    private val folderList = ListLiveData<FolderObservable>()

    fun requestListenFolders(): LiveData<Unit> {
        return linkRepository.listenFolderFromCache().handleLiveList(
            viewModel = this,
            onSuccess = { folders ->
                val observables = folders.map { folder -> folder.toObservable() }
                val sortedObservables = observables.sortedBy { it.order }
                folderList.init(sortedObservables)
            }
        )
    }

    fun requestDeleteFolder(folderId: String) = requestConvert(
        request = linkRepository.deleteFolder(folderId),
        onSuccess = {
            val observable = folderList.getList().first { it.id == folderId }
            folderList.deleteItem(observable)
        }
    )

    fun requestRenameFolder(observable: FolderObservable) = requestConvert(
        request = linkRepository.renameFolder(observable.id, newFolderName),
        onSuccess = {
            val folderObservable = findFolderById(observable.id)
            folderObservable.name = newFolderName
            val indexFolder = folderList.getList().indexOf(observable)
            folderList.insertWithChanged(observable, indexFolder)
        }
    )

    fun requestReorderFolders(): LiveData<ResponseState<Unit>> {
        val folderIds = folderList.getList().map { folder -> folder.id }
        return requestConvert(
            request = linkRepository.reorderFolders(folderIds),
            onSuccess = { emitAction(ACTION_DISMISS) }
        )
    }

    fun getFolderLiveListForSelect() = folderList

    fun setEditObservables(observable: List<FolderObservable>) {
        folderList.change(observable)
    }

    private fun FolderData.toObservable(): FolderObservable {
        return FolderObservable(id, name, order)
    }

    private fun findFolderById(folderId: String): FolderObservable {
        return folderList.getList().first { it.id == folderId }
    }

    companion object {
        const val ACTION_DISMISS = 12211

        private const val TAG = "FOLDER_VIEW_MODEL"
    }
}