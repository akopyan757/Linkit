package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.switchMap
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.linkit.view.scope.mainInject
import com.akopyan757.linkit.viewmodel.observable.AdObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

class PageViewModel(private val folderId: Int): BaseViewModel(), KoinComponent {

    private val linkRepository: LinkRepository by mainInject()

    private val keyCount = Config.KEY_SELECTED_COUNT.format(folderId)
    private val stateHandle: SavedStateHandle by inject(named(Config.HANDLE_URL))
    private val liveDelete by LiveSavedStateBindable<Boolean>(stateHandle, Config.KEY_EDIT_DELETE)

    @get:Bindable var isEmptyPage: Boolean by DB(false, BR.emptyPage)

    @get:Bindable var selectedCount: Int
            by SavedStateBindable(stateHandle, keyCount, 0, BR.selectedCount)

    private val stateEditMode: Boolean by SavedStateBindable(
            savedStateHandle = stateHandle,
            key = Config.KEY_EDIT_MODE_STATE,
            default = false
    )

    private val urlListData = ListLiveData<DiffItemObservable>()

    fun requestDeleteUrls() = liveDelete.switchMap { delete ->
        val deletedIds = if (delete) getLinksIdsFromList() else emptyList()

        requestConvert(
            request = linkRepository.deleteUrls(deletedIds),
            onSuccess = {
                val remainsLinkObservables = getRemainLinksObservable(deletedIds)
                urlListData.change(remainsLinkObservables) { selectedCount = 0 }
            }
        )
    }

    fun getUrlLiveList() = urlListData
    fun isEditMode() = stateEditMode

    fun bindUrlList() {
        bindLiveList(
            request = linkRepository.getUrlLinksByFolder(folderId),
            listLiveData = urlListData,
            onMap = { data ->
                val linkObservables = data.map(LinkObservable::from)
                addAdsObservables(linkObservables)
            },
            onFinished = { observables -> isEmptyPage = observables.isEmpty() }
        )
    }

    fun onLinkItemSelected(observable: LinkObservable) {
        observable.selected = !observable.selected
        urlListData.changeItem(observable) {
            selectedCount = urlListData.getList()
                .mapNotNull { it as? LinkObservable }
                .count { it.selected }
        }
    }

    fun onUrlOpened(observable: LinkObservable) {
        linkRepository.moveLinkToTop(observable.id)
    }

    fun onAdClosed(adObservable: AdObservable) {
        urlListData.deleteItem(adObservable)
    }

    private fun addAdsObservables(list: List<LinkObservable>): List<DiffItemObservable> {
        val linksSize = list.size
        val mutableList = list.toMutableList<DiffItemObservable>()
        (0 until linksSize / (AD_FREQUENCY - 1)).forEach { adIndex ->
            val adObservable = AdObservable(adIndex + 1)
            mutableList.add(adIndex * AD_FREQUENCY + AD_FREQUENCY - 1, adObservable)
        }
        return mutableList
    }

    private fun getLinksIdsFromList(): List<Long> {
        return urlListData.getList()
            .mapNotNull { it as? LinkObservable }
            .filter { it.selected }
            .map { it.id }
    }

    private fun getRemainLinksObservable(removedIds: List<Long>): List<LinkObservable> {
        val list = urlListData.getList().mapNotNull { it as? LinkObservable }
        return list.filterNot { item -> removedIds.contains(item.id) }
    }

    companion object {
        private const val AD_FREQUENCY = 7
    }
}