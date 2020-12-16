package com.akopyan757.linkit.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.common.Config
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
            val imageFileName = item.contentFileName ?: item.logoFileName
            LinkObservable(item.url, item.title, item.description, photoUrl, imageFileName)
        }

        urlListData.change(observables)

        isEmptyPage = observables.isEmpty()
        Log.i(TAG, "GEL URL LINK LIST (FOLDER = $folderId): ${observables.isEmpty()} + ${urlListData.getList().isEmpty()} = $isEmptyPage")
    })

    /**
     * PPublic method
     */
    fun getUrlLiveList() = urlListData

    fun getLiveEditMode() = liveEditModel

    override fun getLiveResponses() = groupLiveResponses(getUrlAllResponse)
}