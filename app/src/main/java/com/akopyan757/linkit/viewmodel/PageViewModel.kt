package com.akopyan757.linkit.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
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

        val observables = data.map { item ->
            Log.i(TAG, "GEL URL LINK: $item")
            val photoUrl = item.photoUrl?.takeUnless { it.isEmpty() } ?: item.logoUrl
            val bitmap = item.contentBitmap ?: item.logoBitmap
            LinkObservable(item.url, item.title, item.description, photoUrl, bitmap)
        }

        urlListData.change(observables)

        isEmptyPage = observables.isEmpty()
        Log.i(TAG, "GEL URL LINK LIST (FOLDER = $folderId): ${observables.isEmpty()} + ${urlListData.getList().isEmpty()} = $isEmptyPage")
    })

    /**
     * PPublic method
     */
    fun getUrlLiveList() = urlListData

    override fun getLiveResponses() = groupLiveResponses(getUrlAllResponse)
}