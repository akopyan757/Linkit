package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit_domain.usecase.folder.RenameFolderUseCase
import com.akopyan757.linkit_model.throwable.FolderExistsException
import org.koin.core.KoinComponent

class FolderRenameViewModel(folderObservable: FolderObservable): BaseViewModel(), KoinComponent {

    private val renameFolder: RenameFolderUseCase by injectUseCase()

    @get:Bindable var folderName: String by DB(folderObservable.name, BR.folderName)
    @get:Bindable var errorName: String by DB("", BR.errorName)

    private val folderId: String = folderObservable.id
    private val oldFolderName: String = folderObservable.name

    fun requestRenameFolder(folderNotSelectedMessage: String) {
        if (folderName == oldFolderName) {
            return
        }

        if (folderName.trim().isEmpty()) {
            errorName = folderNotSelectedMessage
            return
        }

        renameFolder.execute(RenameFolderUseCase.Params(folderId, folderName),
            onSuccess = { emitAction(ACTION_CLOSE) },
            onError = { throwable ->
                if (throwable is FolderExistsException) {
                    errorName = throwable.message ?: ""
                }
            })
    }

    companion object {
        const val ACTION_CLOSE = 112
    }
}