package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.exception.FolderExistsException
import com.akopyan757.linkit.model.repository.FolderRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class FolderCreateViewModel: BaseViewModel(), KoinComponent {

    private val folderRepository: FolderRepository by inject()

    @get:Bindable var folderName: String by DB("", BR.folderName)
    @get:Bindable var errorName: String by DB("", BR.errorName)

    fun requestCreateFolder(): LiveData<ResponseState<Unit>> {
        if (folderName.trim().isEmpty()) return emptyLiveRequest()

        return requestConvert(
            request = folderRepository.createFolder(folderName),
            onSuccess = {},
            onError = { exception ->
                if (exception is FolderExistsException)
                    errorName = exception.message ?: Config.EMPTY
            }
        )
    }

    fun setErrorMessage(message: String) {
        errorName = message
    }

}