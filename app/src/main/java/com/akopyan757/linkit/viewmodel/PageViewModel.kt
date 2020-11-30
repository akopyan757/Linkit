package com.akopyan757.linkit.viewmodel

import android.net.Uri
import android.util.Log
import androidx.databinding.Bindable
import com.akopyan757.linkit.BR
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.common.utils.FormatUtils
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import com.akopyan757.linkit.viewmodel.observable.StoreObservable
import org.koin.core.KoinComponent
import org.koin.core.inject

class PageViewModel(private val folderId: Int): BaseViewModel(), KoinComponent {

    companion object {
        private const val TAG = "PAGE_VIEW_MODEL"
    }

    @get:Bindable
    var isEmptyPage: Boolean by DelegatedBindable(false, BR.emptyPage)

    /**
     * List LiveData's
     */
    private val urlListData = ListLiveData<LinkObservable>()
    private val storeListData = ListLiveData<StoreObservable>()

    /**
     * Repository
     */
    private val linkRepository: LinkRepository by inject()

    /**
     * Responses
     */
    private val getUrlAllResponse = requestLiveData(method = {
        linkRepository.getAllUrlLinksByFolder(folderId)
    }, onSuccess = { data ->
        Log.i(TAG, "GEL URL LINK LIST (FOLDER = $folderId): SIZE = ${data.size}")

        val observables = data.map {
            LinkObservable(it.url, it.title, it.description, it.photoUrl)
        }

        urlListData.change(observables)

        isEmptyPage = observables.isEmpty() && storeListData.getList().isEmpty()
        Log.i(TAG, "GEL URL LINK LIST (FOLDER = $folderId): ${observables.isEmpty()} + ${urlListData.getList().isEmpty()} = $isEmptyPage")
    })

    private val getStoreAllResponse = requestLiveData(method = {
        linkRepository.getAllStoreLinksByFolder(folderId)
    }, onSuccess = { data ->
        Log.i(TAG, "GEL STORE LINK LIST (FOLDER = $folderId): SIZE = ${data.size}")

        val observables = data.map {
            StoreObservable(Uri.parse(it.uri), it.name, FormatUtils.getSize(it.size), it.path)
        }

        storeListData.change(observables)

        isEmptyPage = observables.isEmpty() && urlListData.getList().isEmpty()
        Log.i(TAG, "GEL STORE LINK LIST (FOLDER = $folderId): ${observables.isEmpty()} + ${urlListData.getList().isEmpty()} = $isEmptyPage")
    })


    /**
     * PPublic method
     */
    fun getUrlLiveList() = urlListData
    fun getStoreLiveList() = storeListData

    override fun getLiveResponses() = groupLiveResponses(getUrlAllResponse, getStoreAllResponse)
}