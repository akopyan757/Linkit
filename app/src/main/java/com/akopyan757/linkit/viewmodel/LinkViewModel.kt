package com.akopyan757.linkit.viewmodel

import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import org.koin.core.KoinComponent
import org.koin.core.inject

class LinkViewModel : BaseViewModel(), KoinComponent {

    companion object {
        private const val TAG = "LINK_VIEW_MODEL"
    }

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

    fun getFolderLiveList() = foldersLiveData

    override fun getLiveResponses() = groupLiveResponses(getAllFolder)
}