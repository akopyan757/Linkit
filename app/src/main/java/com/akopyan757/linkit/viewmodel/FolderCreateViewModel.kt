package com.akopyan757.linkit.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.exception.FolderExistsException
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.base.viewmodel.BaseViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class FolderCreateViewModel: BaseViewModel(), KoinComponent {

    companion object {
        private const val TAG = "FOLDER_CREATE_VM"
    }

    private var resourceEmptyName: String = ""

    /** Bindable properties */
    @get:Bindable
    var folderName: String by DelegatedBindable("", BR.folderName)

    @get:Bindable
    var errorName: String by DelegatedBindable("", BR.errorName)

    /** Repository */
    private val linkRepository: LinkRepository by inject()

    /** Request */
    private val addFolderRequest = MutableLiveData<String>()

    /** Responses */
    private val addFolderResponse = addFolderRequest.switchMap { name ->
        requestLiveData({
            linkRepository.addNewFolder(name)
        }, onLoading = {
            Log.i(TAG, "ADD NEW FOLDER: LOADING: NAME = $name")
        }, onSuccess = {
            Log.i(TAG, "ADD NEW FOLDER: SUCCESS: ADDED FOLDER = $name")
        }, onError = { exception ->
            Log.e(TAG, "ADD NEW FOLDER: ERROR", exception)
            if (exception is FolderExistsException) {
                errorName = exception.message ?: Config.EMPTY
            }
        })
    }

    override fun getLiveResponses() = groupLiveResponses(addFolderResponse)

    fun initResources(emptyName: String) {
        resourceEmptyName = emptyName
    }

    fun onCreateFolderButtonClicked() {
        val name = folderName.trim()
        if (name.isEmpty()) {
            errorName = resourceEmptyName
        } else {
            addFolderRequest.value = name
        }
    }

}