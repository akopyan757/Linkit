package com.akopyan757.linkit.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.switchMap
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.linkit.view.scope.mainInject
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

class PageViewModel(private val folderId: Int): BaseViewModel(), KoinComponent {

    companion object {
        private const val TAG = "PAGE_VIEW_MODEL"
    }

    private val keyCount = Config.KEY_SELECTED_COUNT.format(folderId)

    private val stateHandle: SavedStateHandle by inject(named(Config.HANDLE_URL))

    private val liveEditModel by LiveSavedStateBindable<Boolean>(stateHandle, Config.KEY_EDIT_MODE)
    private val liveEditSave by LiveSavedStateBindable<Boolean>(stateHandle, Config.KEY_EDIT_SAVE)
    private val liveDelete by LiveSavedStateBindable<Boolean>(stateHandle, Config.KEY_EDIT_DELETE)

    @get:Bindable
    var isEmptyPage: Boolean by DelegatedBindable(false, BR.emptyPage)

    @get:Bindable
    var selectedCount: Int by SavedStateBindable(stateHandle, keyCount, 0, BR.selectedCount)

    /** List LiveData's */
    private val urlListData = ListLiveData<LinkObservable>()

    /** Repository */
    private val linkRepository: LinkRepository by mainInject()

    /** Responses */
    init {
        bindLiveList(
            request = linkRepository.getUrlLinksByFolder(folderId),
            listLiveData = urlListData,
            onMap = { data -> data.map(LinkObservable::from) },
            onFinished = { observables -> isEmptyPage = observables.isEmpty() }
        )
    }

    private val saveOrderResponse = liveEditSave.switchMap { savedState ->
        if (savedState) {
            val ids = urlListData.getList().map { item -> item.id }
            val value = ids.joinToString(", ")

            requestLiveData(
                method = { linkRepository.reorderLinks(ids) },
                onLoading = { Log.i(TAG, "REORDER URLS: LOADING") },
                onSuccess = { Log.i(TAG, "REORDER URLS: SUCCESS: $value") },
                onError = { exception -> Log.i(TAG, "REORDER URLS: ERROR", exception) }
            )
        } else {
            requestLiveData(method = {
                linkRepository.getUrlLinksByFolder2(folderId)
            }, onSuccess = { data ->
                val observables = data.map(LinkObservable::from)
                urlListData.change(observables)
                isEmptyPage = observables.isEmpty()
            })
        }
    }

    private val deleteUrlsResponse = liveDelete.switchMap { delete ->
        val ids = if (delete) {
            urlListData.getList().mapNotNull { if (it.selected) it.id else null }
        } else emptyList()

        requestLiveData(
            method = { linkRepository.deleteUrls(ids) },
            onSuccess = {
                val observables = urlListData.getList().filterNot { item -> ids.contains(item.id) }
                urlListData.change(observables) {
                    selectedCount = 0
                }
            }
        )
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
        urlListData.changeItem(observable) {
            selectedCount = urlListData.getList().count { it.selected }
        }
    }

    override fun getLiveResponses() = groupLiveResponses(
        saveOrderResponse, deleteUrlsResponse
    )
}