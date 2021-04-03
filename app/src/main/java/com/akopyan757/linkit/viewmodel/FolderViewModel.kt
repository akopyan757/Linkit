package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.repository.FolderRepository
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import org.koin.core.KoinComponent
import org.koin.core.inject

class FolderViewModel : BaseViewModel(), KoinComponent {

    private val folderRepository: FolderRepository by inject()

    @get:Bindable var newFolderName: String by DB("", BR.newFolderName)

    private val folderList = ListLiveData<FolderObservable>()

    fun requestListenFolders(): LiveData<Unit> {
        return folderRepository.listenFolderFromCache().handleLiveList(
            viewModel = this,
            onSuccess = { folders ->
                val observables = folders.map { folder -> FolderObservable.fromData(folder) }
                val sortedObservables = observables.sortedBy { it.order }
                folderList.init(sortedObservables)
            }
        )
    }

    fun requestDeleteFolder(folderId: String) = requestConvert<Unit, Unit>(
        request = folderRepository.deleteFolder(folderId)
    )

    fun requestRenameFolder(observable: FolderObservable) = requestConvert<Unit, Unit>(
        request = folderRepository.renameFolder(observable.id, newFolderName)
    )

    fun requestReorderFolders(): LiveData<ResponseState<Unit>> {
        val folderIds = folderList.getList().map { folder -> folder.id }
        return requestConvert(
            request = folderRepository.reorderFolders(folderIds),
            onSuccess = { emitAction(ACTION_DISMISS) }
        )
    }

    fun getFolderLiveListForSelect() = folderList

    fun setEditObservables(observable: List<FolderObservable>) {
        folderList.change(observable)
    }

    companion object {
        const val ACTION_DISMISS = 12211

        private const val TAG = "FOLDER_VIEW_MODEL"
    }
}