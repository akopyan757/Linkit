package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.Config.KEY_EDIT_DELETE
import com.akopyan757.linkit.common.Config.KEY_SELECTED_COUNT
import com.akopyan757.linkit.common.utils.SumLiveData
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.linkit.view.scope.mainInject
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

class LinkViewModel : BaseViewModel(), KoinComponent {

    private val stateHandle: SavedStateHandle by inject(named(Config.HANDLE_URL))

    @get:Bindable
    var editMode: Boolean by SavedStateBindable(
        stateHandle, Config.KEY_EDIT_MODE_STATE, false, BR.editMode
    )

    @get:Bindable var isFoldersEmpty: Boolean by DelegatedBindable(false, BR.foldersEmpty)

    @get:Bindable var profileIconUrl: String? by DelegatedBindable(null, BR.profileIconUrl)

    @get:Bindable var profileIconDefaultRes: Int = R.drawable.ic_user

    private var deleteAction: Boolean by SavedStateBindable(stateHandle, KEY_EDIT_DELETE, false)

    private val selectedCount = SumLiveData()

    private val deleteUrlsVisible = selectedCount.map { count ->
        editMode = count > 0
        count > 0
    }

    /** List LiveData's */
    private val foldersLiveData = ListLiveData<FolderObservable>()

    /** Repository */
    private val linkRepository: LinkRepository by mainInject()

    /** Init */

    /** Responses */
    fun bindAllFolders() {
        bindLiveList(
            request = linkRepository.getAllFolders(),
            listLiveData = foldersLiveData,
            onStart = { selectedCount.removeAll() },
            onMap = { list ->
                list.map { folder ->
                    val name = KEY_SELECTED_COUNT.format(folder.id)
                    val source by LiveSavedStateBindable<Int>(stateHandle, name)
                    selectedCount.addSource(source, name)
                    FolderObservable(folder.id, folder.name, 1)
                }
            }, onFinished = { list ->
                isFoldersEmpty = list.count() <= 1
            }
        )
    }

    override fun getLiveResponses(): LiveData<RequestState> {
        return requestLiveData(method = { linkRepository.initResources() })
    }

    /**
     * Public methods
     */
    fun disableEditMode() {
        editMode = false
    }

    fun deleteSelected() {
        deleteAction = true
    }

    fun setProfileUrl(url: String) {
        profileIconUrl = url
    }

    fun getFolderLiveList() = foldersLiveData

    fun getDeleteIconVisible() = deleteUrlsVisible

    fun getSelectedCount() = selectedCount

    fun getLivePattern() = linkRepository.getLivePattern()
}