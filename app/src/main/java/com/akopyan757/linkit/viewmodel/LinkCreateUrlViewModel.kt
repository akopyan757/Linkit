package com.akopyan757.linkit.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
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
    var title: String by DelegatedBindable("", BR.title, BR.enabledButton)

    @get:Bindable
    var description: String by DelegatedBindable("", BR.description)

    @get:Bindable
    var selectedFolderName: String by DelegatedBindable("", BR.selectedFolderName, BR.enabledButton)

    @get:Bindable
    val enabledButton: Boolean
        get() = selectedFolderName.isNotEmpty() && title.isNotEmpty()

    private lateinit var defaultUrlLinkData: UrlLinkData

    /** ListView */
    private var foldersList = ListLiveData<FolderObservable>()

    /** Repository */
    private val linkRepository: LinkRepository by inject()

    /** Requests */
    private val addLinkRequest = MutableLiveData<UrlLinkData>()

    /** Responses */
    init {
        bindLiveList(
            request = linkRepository.getAllFolders(),
            listLiveData = foldersList,
            onMap = { folders -> folders.map { FolderObservable(it.id, it.name, it.order) } },
            onFinished = { Log.i(TAG, "GET FOLDER LIST: SUCCESS") },
            onError = { exception -> Log.i(TAG, "GET FOLDER LIST: ERROR", exception) }
        )
    }

    private val getInfoFromUrl = requestLiveData(
        method = { linkRepository.getDefaultInfoFromUrl(url) },
        onLoading = {
            Log.i(TAG, "GET INFO URL: LOADING")
        }, onSuccess = { urlLinkData ->
            Log.i(TAG, "GET INFO URL: SUCCESS: $urlLinkData")
            defaultUrlLinkData = urlLinkData
            title = urlLinkData.title
            description = urlLinkData.description
        }, onError = { exception ->
            Log.e(TAG, "GET INFO URL: ERROR", exception)
        }
    )

    private val addLinkResponse = addLinkRequest.switchMap { urlLinkData ->
        requestLiveData(
            method = { linkRepository.addNewLink(urlLinkData) },
            onLoading = { Log.i(TAG, "ADD LINK: LOADING") },
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
        val folderId = foldersList.getList()
            .find { it.name == selectedFolderName }
            ?.id  ?: FolderData.GENERAL_FOLDER_ID

        defaultUrlLinkData.folderId = folderId
        defaultUrlLinkData.title = title
        defaultUrlLinkData.description = description
        addLinkRequest.value = defaultUrlLinkData
    }

    override fun getLiveResponses() = groupLiveResponses(
        getInfoFromUrl, addLinkResponse
    )

    fun getFolderNameList() = foldersList.map { holder -> holder.data.map { it.name } }
}