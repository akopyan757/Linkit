package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.linkit.view.scope.mainInject
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import org.koin.core.KoinComponent

class FolderViewModel : BaseViewModel(), KoinComponent {

    private val linkRepository: LinkRepository by mainInject()

    @get:Bindable var newFolderName: String by DB("", BR.newFolderName)

    private val foldersSelectedLiveData = ListLiveData<FolderObservable>()

    fun bindFoldersList() {
        bindLiveList(
            request = linkRepository.getAllFolders(),
            listLiveData = foldersSelectedLiveData,
            onMap = { data -> convertFoldersDataToObservables(data) },
            onFinished = { observables -> if (observables.isEmpty()) emitAction(ACTION_DISMISS) }
        )
    }

    fun requestDeleteFolder(folderId: Int) = requestConvert(
        request = linkRepository.deleteFolder(folderId),
        onSuccess = {
            val observable = foldersSelectedLiveData.getList().first { it.id == folderId }
            foldersSelectedLiveData.deleteItem(observable)
        }
    )

    fun requestRenameFolder(observable: FolderObservable) = requestConvert(
        request = linkRepository.renameFolder(observable.id, newFolderName),
        onSuccess = {
            val folderObservable = findFolderById(observable.id)
            folderObservable.name = newFolderName
            val indexFolder = foldersSelectedLiveData.getList().indexOf(observable)
            foldersSelectedLiveData.insertWithChanged(observable, indexFolder)
        }
    )

    fun requestReorderFolders() = requestConvert(
        request = linkRepository.reorderFolders(getFoldersOrderList()),
        onSuccess = { emitAction(ACTION_DISMISS) }
    )

    fun getFolderLiveListForSelect() = foldersSelectedLiveData

    fun setEditObservables(observable: List<FolderObservable>) {
        foldersSelectedLiveData.change(observable)
    }

    private fun convertFoldersDataToObservables(folders: List<FolderData>): List<FolderObservable> {
        return folders.map { item ->
            FolderObservable(item.id, item.name)
        }.filter { observable ->
            observable.id != FolderData.GENERAL_FOLDER_ID
        }
    }

    private fun getFoldersOrderList(): List<Pair<Int, Int>> {
        return foldersSelectedLiveData.getList()
                .mapIndexed { index, observable -> observable.id to index }
    }

    private fun findFolderById(folderId: Int): FolderObservable {
        return foldersSelectedLiveData.getList().first { it.id == folderId }
    }

    companion object {
        const val ACTION_DISMISS = 12211

        private const val TAG = "FOLDER_VIEW_MODEL"
    }
}