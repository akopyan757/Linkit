package com.akopyan757.linkit.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.entity.UrlLinkData
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

class PageViewModel(private val folderId: Int): BaseViewModel(), KoinComponent {

    companion object {
        private const val TAG = "PAGE_VIEW_MODEL"
    }

    private val stateHandle: SavedStateHandle by inject(named(Config.HANDLE_URL))

    private val liveEditModel by LiveSavedStateBindable<Boolean>(stateHandle, Config.KEY_EDIT_MODE)
    private val liveEditSave by LiveSavedStateBindable<Boolean>(stateHandle, Config.KEY_EDIT_SAVE)

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
        linkRepository.getUrlLinksByFolder(folderId)
    }, onSuccess = { data ->
        Log.i(TAG, "GEL URL LINK LIST (FOLDER = $folderId): SIZE = ${data.size}")

        val observables = data.map { item -> item.toObservable() }
        urlListData.change(observables)
        isEmptyPage = observables.isEmpty()
        Log.i(TAG, "GEL URL LINK LIST (FOLDER = $folderId): ${observables.isEmpty()} + ${urlListData.getList().isEmpty()} = $isEmptyPage")
    })

    private val saveOrderResponse = liveEditSave.switchMap { savedState ->
        if (savedState) {
            val pairs = urlListData.getList().mapIndexed { index, item -> Pair(item.id, index) }

            requestLiveData(method = {
                linkRepository.reorderLinks(pairs)
            }, onLoading = {
                Log.i(TAG, "REORDER URLS: LOADING")
            }, onSuccess = {
                val value = pairs.joinToString(", ") { "[${it.first}: order=${it.second}]" }
                Log.i(TAG, "REORDER URLS: SUCCESS: $value")

            }, onError = { exception ->
                Log.i(TAG, "REORDER URLS: ERROR", exception)
            })

        } else {

            requestLiveData(method = {
                linkRepository.getUrlLinksByFolder(folderId, isLive=false)
            }, onSuccess = { data ->
                val observables = data.map { item -> item.toObservable() }
                urlListData.change(observables)
                isEmptyPage = observables.isEmpty()
            })
        }
    }

    /**
     * PPublic method
     */
    fun getUrlLiveList() = urlListData

    fun getLiveEditMode() = liveEditModel

    fun setEditObservables(observable: List<LinkObservable>) {
        urlListData.change(observable)
    }

    fun selectItem(observable: LinkObservable) {
        observable.selected = !observable.selected
        urlListData.changeItem(observable)
    }

    /**
     * Private method
     */
    private fun UrlLinkData.toObservable(): LinkObservable {
        val photoUrl = photoUrl?.takeUnless { it.isEmpty() } ?: logoUrl
        val imageFileName = contentFileName ?: logoFileName
        return LinkObservable(id, url, title, description, photoUrl, imageFileName)
    }

    override fun getLiveResponses() = groupLiveResponses(getUrlAllResponse, saveOrderResponse)
}