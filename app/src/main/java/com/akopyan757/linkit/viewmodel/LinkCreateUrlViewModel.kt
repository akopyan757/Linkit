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
import com.akopyan757.linkit.view.scope.mainInject
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import org.koin.core.KoinComponent

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
    var selectedFolderName: String by DelegatedBindable("", BR.selectedFolderName)

    /** ListView */
    private var foldersList = ListLiveData<FolderObservable>()

    /** Repository */
    private val linkRepository: LinkRepository by mainInject()

    /** Requests */
    private val addLinkRequest = MutableLiveData<Any>()

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

    private val addLinkResponse = addLinkRequest.switchMap {
        val folderId: Int = getSelectedFolderIdOrDefault()
        requestLiveData(
            method = { linkRepository.addNewLink(url, folderId) },
            onSuccess = { this emitAction ACTION_DISMISS },
        )
    }


    /** Public methods */
    fun initResources(notSelected: String) {
        resourceNotSelected = notSelected
    }

    fun onAcceptUrl() {
        addLinkRequest.value = Any()
    }

    override fun getLiveResponses() = groupLiveResponses(
        addLinkResponse
    )

    fun getFolderNameList() = foldersList.map { holder -> holder.data.map { it.name } }

    private fun getSelectedFolderIdOrDefault(): Int {
        val folder = foldersList.getList().find { it.name == selectedFolderName }
        return folder?.id ?: FolderData.GENERAL_FOLDER_ID
    }
}