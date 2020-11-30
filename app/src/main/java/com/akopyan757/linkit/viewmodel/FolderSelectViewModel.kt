package com.akopyan757.linkit.viewmodel

import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import org.koin.core.KoinComponent
import org.koin.core.inject

class FolderSelectViewModel : BaseViewModel(), KoinComponent {

    private var resourceNotSelected: String = ""

    /**
     * List LiveData's
     */
    private val foldersSelectedLiveData = ListLiveData<FolderObservable>()

    /**
     * Repository
     */
    private val linkRepository: LinkRepository by inject()

    /**
     * Responses
     */
    private val getAllFolder = requestLiveData(
        method = { linkRepository.getAllFolders() },
        onSuccess = { list ->
            val observables = list.mapIndexed { index, folder ->
                FolderObservable(folder.id, folder.name, index % 2 + 1)
            }

            val updatedObservable = observables.filter { it.id != FolderData.GENERAL_FOLDER_ID }

            foldersSelectedLiveData.change(updatedObservable)
        }
    )

    fun initResources(notSelected: String) {
        resourceNotSelected = notSelected
    }

    fun getFolderLiveListForSelect() = foldersSelectedLiveData

    override fun getLiveResponses() = groupLiveResponses(getAllFolder)
}