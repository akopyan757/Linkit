package com.akopyan757.linkit.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.akopyan757.linkit.BR
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.request.LinkRequest
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.URL

class LinkCreateUrlViewModel: BaseViewModel(), KoinComponent {

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
    var urlValue: String by DelegatedBindable("", BR.urlValue)

    /**
     * ListView
     */
    private val foldersList = ListLiveData<FolderObservable>()

    /**
     * Repository
     */
    private val linkRepository: LinkRepository by inject()

    /** Request */
    private val findFolderByRuleRequest = MutableLiveData<String>()
    private val addLinkRequest = MutableLiveData<LinkRequest>()
    private val getUrlInfoRequest = MutableLiveData<String>()

    /** Responses */

    private val getInfoFromUrl = getUrlInfoRequest.switchMap { url ->
        requestLiveData(
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
    }

    private val addLinkResponse = addLinkRequest.switchMap {
        requestLiveData(
            method = { linkRepository.addNewLink(it.link, it.folderId, it.title, it.description) },
            onLoading = {
                Log.i(TAG, "ADD LINK: LOADING")
            },
            onSuccess = { data ->
                if (data is UrlLinkData) {
                    Log.i(TAG, "ADD LINK: SUCCESS")
                    this emitAction ACTION_DISMISS
                }
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

    fun onAcceptUrl(url: String? = null) {
        val selectedUrl = url ?: urlValue
        val folderIds = foldersList.getList().map { it.id }
        addLinkRequest.value = LinkRequest(title, description, selectedUrl, folderIds)
    }

    fun setupUrl(url: String) {
        Log.i(TAG, "Setup Url = $url")
        urlValue = url
        findFolderByRuleRequest.value = URL(url).host
        getUrlInfoRequest.value = url
    }

    fun setFolderList(folders: List<FolderObservable>) {
        foldersList.change(folders)
    }

    override fun getLiveResponses() = groupLiveResponses(
        getInfoFromUrl, addLinkResponse
    )

    fun getFolderLiveList() = foldersList
}