package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.exception.FolderExistsException
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.linkit.view.scope.mainInject
import org.koin.core.KoinComponent

class FolderCreateViewModel: BaseViewModel(), KoinComponent {

    private val linkRepository: LinkRepository by mainInject()

    @get:Bindable var folderName: String by DB("", BR.folderName)
    @get:Bindable var errorName: String by DB("", BR.errorName)

    fun requestCreateFolder(): LiveData<ResponseState<Unit>> {
        if (folderName.trim().isEmpty()) {
            return emptyLiveRequest()
        }

        return requestConvert(
            request = linkRepository.addNewFolder(folderName),
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