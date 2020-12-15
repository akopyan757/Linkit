package com.akopyan757.linkit.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.request.LinkRequest
import org.koin.core.KoinComponent
import org.koin.core.inject

class LinkCreateUrlViewModel(
    @get:Bindable val url: String
): BaseViewModel(), KoinComponent {

    companion object {
        private const val TAG = "LINK_CREATE_URL_VM"

        const val ACTION_DISMISS = 121_1
    }

    /**
     * String resources
     */
    private var resourceNotSelected: String = ""

    /**
     * Binding properties
     */
    @get:Bindable
    var title: String by DelegatedBindable("", BR.title)

    @get:Bindable
    var description: String by DelegatedBindable("", BR.description)

    @get:Bindable
    var selectedFolderName by DelegatedBindable("", BR.selectedFolderName)

    /**
     * ListView
     */
    private var foldersList: MutableLiveData<List<FolderObservable>> = MutableLiveData()

    /**
     * Repository
     */
    private val linkRepository: LinkRepository by inject()

    /** Requests */
    private val addLinkRequest = MutableLiveData<LinkRequest>()

    /** Responses */
    private val getListFolders = requestLiveData(
            method = { linkRepository.getAllFolders() },
            onLoading = {
                Log.i(TAG, "GET FOLDER LIST: LOADING")
            }, onSuccess = { folders ->
                val names = folders.toTypedArray()
                Log.i(TAG, "GET FOLDER LIST: SUCCESS: ${names.joinToString(", ")}")
                foldersList.value = names.map { FolderObservable(it.id, it.name, it.order) }
            }, onError = { exception ->
                Log.i(TAG, "GET FOLDER LIST: ERROR", exception)
            }
    )

    private val getInfoFromUrl = requestLiveData(
        method = { linkRepository.getDefaultInfoFromUrl(url) },
        onLoading = {
            Log.i(TAG, "GET INFO URL: LOADING")
        }, onSuccess = { urlLinkData ->
            Log.i(TAG, "GET INFO URL: SUCCESS: $urlLinkData")
            title = urlLinkData.title
            description = urlLinkData.description
        }, onError = { exception ->
            Log.e(TAG, "GET INFO URL: ERROR", exception)
        }
    )

    private val addLinkResponse = addLinkRequest.switchMap {
        requestLiveData(
            method = { linkRepository.addNewLink(it.link, it.folderId, it.title, it.description) },
            onLoading = {
                Log.i(TAG, "ADD LINK: LOADING")
            },
            onSuccess = {
                Log.i(TAG, "ADD LINK: SUCCESS")
                this emitAction ACTION_DISMISS
            },
            onError = { exception ->
                Log.e(TAG, "ADD LINK: ERROR", exception)
            }
        )
    }

    /** Public methods */
    fun initResources(notSelected: String) {
        resourceNotSelected = notSelected
    }

    fun onAcceptUrl() {
        val folderId = foldersList.value?.find { it.name == selectedFolderName }?.id  ?: FolderData.GENERAL_FOLDER_ID
        addLinkRequest.value = LinkRequest(title, description, url, folderId)
    }

    override fun getLiveResponses() = groupLiveResponses(
        getInfoFromUrl, addLinkResponse, getListFolders
    )

    fun getFolderList(): LiveData<Array<String>> = foldersList.map {
        list -> list.map { it.name }.toTypedArray()
    }
}