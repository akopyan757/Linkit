package com.akopyan757.linkit.viewmodel

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

    private val keyCount = Config.KEY_SELECTED_COUNT.format(folderId)

    private val stateHandle: SavedStateHandle by inject(named(Config.HANDLE_URL))

    private val liveDelete by LiveSavedStateBindable<Boolean>(stateHandle, Config.KEY_EDIT_DELETE)

    @get:Bindable
    var isEmptyPage: Boolean by DelegatedBindable(false, BR.emptyPage)

    @get:Bindable
    var selectedCount: Int by SavedStateBindable(stateHandle, keyCount, 0, BR.selectedCount)

    /** Edit Mode state*/
    private val stateEditMode: Boolean by SavedStateBindable(
            savedStateHandle = stateHandle,
            key = Config.KEY_EDIT_MODE_STATE,
            default = false
    )

    /** List LiveData's */
    private val urlListData = ListLiveData<LinkObservable>()

    /** Repository */
    private val linkRepository: LinkRepository by mainInject()

    /** Responses */
    fun getDeleteUrlsResponseLive() = liveDelete.switchMap { delete ->
        val ids = if (delete) {
            urlListData.getList()
                    .filter { it.selected }
                    .map { it.id }
        } else emptyList()

        requestConvertSimple<Unit>(
            method = { linkRepository.deleteUrls(ids) },
            onSuccess = {
                val list = urlListData.getList()
                val observables = list.filterNot { item -> ids.contains(item.id) }
                urlListData.change(observables) { selectedCount = 0 }
            }
        )
    }

    /**
     * Public method
     */
    fun getUrlLiveList() = urlListData

    fun getEditModeState() = stateEditMode

    fun setEditObservables(observable: List<LinkObservable>) {
        urlListData.change(observable)
    }

    fun bindUrlList() {
        bindLiveList(
            request = linkRepository.getUrlLinksByFolder(folderId),
            listLiveData = urlListData,
            onMap = { data -> data.map(LinkObservable::from) },
            onFinished = { observables -> isEmptyPage = observables.isEmpty() }
        )
    }

    fun onItemSelected(observable: LinkObservable) {
        observable.selected = !observable.selected
        urlListData.changeItem(observable) {
            selectedCount = urlListData.getList().count { it.selected }
        }
    }

    fun onUrlOpened(observable: LinkObservable) {
        linkRepository.moveLinkToTop(observable.id)
    }
}