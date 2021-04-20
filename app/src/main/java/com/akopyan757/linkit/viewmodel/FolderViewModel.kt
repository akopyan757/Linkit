package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit_domain.usecase.folder.DeleteFolderUseCase
import com.akopyan757.linkit_domain.usecase.folder.ListenFoldersChangesUseCase
import com.akopyan757.linkit_domain.usecase.folder.RenameFolderUseCase
import com.akopyan757.linkit_domain.usecase.folder.ReorderFolderUseCase
import org.koin.core.KoinComponent

class FolderViewModel : BaseViewModel(), KoinComponent {

    private val reorderFolders: ReorderFolderUseCase by injectUseCase()
    private val deleteFolder: DeleteFolderUseCase by injectUseCase()
    private val renameFolder: RenameFolderUseCase by injectUseCase()
    private val listenFoldersChanges: ListenFoldersChangesUseCase by injectUseCase()


    @get:Bindable var newFolderName: String by DB("", BR.newFolderName)

    private val folderList = ListLiveData<FolderObservable>()

    fun startListenFolders() {
        listenFoldersChanges.execute(
            onNext = { folders ->
                val observables = folders.map { folder -> FolderObservable.fromData(folder) }
                val sortedObservables = observables.sortedBy { it.order }
                folderList.init(sortedObservables)
            }
        )
    }

    fun deleteFolder(folderId: String) {
        deleteFolder.execute(DeleteFolderUseCase.Params(folderId))
    }

    fun renameFolder(observable: FolderObservable) {
        renameFolder.execute(RenameFolderUseCase.Params(observable.id, newFolderName))
    }

    fun reorderFolder() {
        val folderIds = folderList.getList().map { folder -> folder.id }
        reorderFolders.execute(ReorderFolderUseCase.Params(folderIds),
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