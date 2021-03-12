package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.map
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.linkit.view.scope.mainInject
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import org.koin.core.KoinComponent

class LinkCreateUrlViewModel(
    @get:Bindable val url: String
): BaseViewModel(), KoinComponent {

    companion object {
        const val ACTION_DISMISS = 121_1
    }

    private val linkRepository: LinkRepository by mainInject()

    @get:Bindable
    var selectedFolderName: String by DelegatedBindable("", BR.selectedFolderName)

    private var foldersList = ListLiveData<FolderObservable>()

    /** Responses */
    fun bindFolderList() {
        bindLiveList(
            request = linkRepository.getAllFolders(),
            listLiveData = foldersList,
            onMap = { folders -> folders.map { FolderObservable(it.id, it.name, it.order) } },
        )
    }

    fun requestCreateNewLink() = requestConvert(
        request = linkRepository.addNewLink(url, getSelectedFolderIdOrDefault()),
        onSuccess = { emitAction(ACTION_DISMISS) },
    )

    fun getFolderNameList() = foldersList.map { holder ->
        holder.data.map { folder -> folder.name }
    }

    private fun getSelectedFolderIdOrDefault(): Int {
        val folder = foldersList.getList().find { it.name == selectedFolderName }
        return folder?.id ?: FolderData.GENERAL_FOLDER_ID
    }
}