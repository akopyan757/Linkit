package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.exception.FolderExistsException
import com.akopyan757.linkit_domain.usecase.folder.CreateFolderUseCase
import org.koin.core.KoinComponent
import org.koin.core.inject

class FolderCreateViewModel: BaseViewModel(), KoinComponent {

    private val createFolder: CreateFolderUseCase by injectUseCase()

    @get:Bindable var folderName: String by DB("", BR.folderName)
    @get:Bindable var errorName: String by DB("", BR.errorName)

    fun requestCreateFolder(folderNotSelectedMessage: String) {
        if (folderName.trim().isEmpty()) {
            errorName = folderNotSelectedMessage
        } else {
            createFolder.execute(CreateFolderUseCase.Params(folderName),
                onSuccess = { emitAction(ACTION_CLOSE) },
                onError = { throwable ->
                    if (throwable is FolderExistsException) {
                        errorName = throwable.message ?: ""
                    }
                })
        }
    }

    companion object {
        const val ACTION_CLOSE = 112
    }
}