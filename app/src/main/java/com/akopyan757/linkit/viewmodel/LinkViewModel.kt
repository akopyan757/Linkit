package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.Config.KEY_EDIT_MODE
import com.akopyan757.linkit.common.Config.KEY_EDIT_SAVE
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

class LinkViewModel : BaseViewModel(), KoinComponent {

    private val stateHandle: SavedStateHandle by inject(named(Config.HANDLE_URL))

    @get:Bindable
    var editMode: Boolean by SavedStateBindable(stateHandle, KEY_EDIT_MODE, false, BR.editMode)

    @get:Bindable
    var savedState: Boolean by SavedStateBindable(stateHandle, KEY_EDIT_SAVE, false, BR.savedState)

    /**
     * List LiveData's
     */
    private val foldersLiveData = ListLiveData<FolderObservable>()

    /**
     * Repository
     */
    private val linkRepository: LinkRepository by inject()

    private val getAllFolder = requestLiveData(
        method = { linkRepository.getAllFolders() },
        onSuccess = { list ->
            val observables = list.mapIndexed { index, folder ->
                FolderObservable(folder.id, folder.name, index % 2 + 1)
            }

            foldersLiveData.init(observables)
        }
    )

    /**
     * Public methods
     */
    fun enableEditMode() {
        editMode = true
    }

    fun disableEditMode() {
        editMode = false
    }

    fun saveEdit() {
        savedState = true
        editMode = false
    }

    fun getFolderLiveList() = foldersLiveData

    override fun getLiveResponses() = groupLiveResponses(getAllFolder)
}