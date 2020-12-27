package com.akopyan757.linkit.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.linkit.view.scope.mainInject
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import org.koin.core.KoinComponent

class FolderViewModel : BaseViewModel(), KoinComponent {

    @get:Bindable
    var newFolderName: String by DelegatedBindable("", BR.newFolderName)

    /** List LiveData's */
    private val foldersSelectedLiveData = ListLiveData<FolderObservable>()

    /** Repository */
    private val linkRepository: LinkRepository by mainInject()

    /** Requests */
    private val requestDelete = MutableLiveData<Int>()
    private val requestEdit = MutableLiveData<Pair<Int, String>>()
    private val requestSave = MutableLiveData<List<Pair<Int, Int>>>()

    /**
     * Responses
     */
    init {
        bindLiveList(
            request = linkRepository.getAllFolders(),
            listLiveData = foldersSelectedLiveData,
            onMap = { data ->
                data.map { item -> FolderObservable(item.id, item.name, 1) }
                    .filter { observable -> observable.id != FolderData.GENERAL_FOLDER_ID }
            }, onFinished = { observables ->
                Log.i(TAG, "Observables=${observables.joinToString(", ")}")
                if (observables.isEmpty()) {
                    this emitAction ACTION_DISMISS
                }
            }
        )
    }

    private val responseDeleteFolder = requestDelete.switchMap { folderId ->
        requestLiveData(
            method = { linkRepository.deleteFolder(folderId) },
            onLoading = {
                Log.i(TAG, "DELETE FOLDER: LOADING: ID = $folderId")
            }, onSuccess = {
                Log.i(TAG, "DELETE FOLDER: SUCCESS: ID = $folderId")
                val observable = foldersSelectedLiveData.getList().first { it.id == folderId }
                foldersSelectedLiveData.deleteItem(observable)
            }, onError = { exception ->
                Log.e(TAG, "DELETE FOLDER: ERROR", exception)
            }
        )
    }

    private val responseEditFolder = requestEdit.switchMap { (folderId, folderName) ->
        requestLiveData(
            method = { linkRepository.renameFolder(folderId, folderName) },
            onLoading = {
                Log.i(TAG, "RENAME FOLDER: LOADING: ID = $folderId; NEW NAME = $folderName")
            }, onSuccess = {
                Log.i(TAG, "RENAME FOLDER: SUCCESS: ID = $folderId")
                val observable = foldersSelectedLiveData.getList()
                        .first { it.id == folderId }
                        .apply { name = folderName }
                val index = foldersSelectedLiveData.getList().indexOf(observable)
                foldersSelectedLiveData.insertWithChanged(observable, index)
            }, onError = { exception ->
                Log.e(TAG, "RENAME FOLDER: ERROR", exception)
            }
        )
    }

    private val responseReorderFolder = requestSave.switchMap { pairs ->
        requestLiveData(
            method = { linkRepository.reorderFolders(pairs) },
            onLoading = {
                val ids = pairs.joinToString(", ") { "[${it.first}: order = ${it.second}]" }
                Log.i(TAG, "REORDER FOLDER: LOADING: $ids")
            }, onSuccess = {
                this emitAction ACTION_DISMISS
                Log.i(TAG, "REORDER FOLDER: SUCCESS")
            }, onError = { exception ->
                Log.i(TAG, "REORDER FOLDER: ERROR", exception)
            }
        )
    }

    /**
     * Public methods
     */
    fun getFolderLiveListForSelect() = foldersSelectedLiveData

    fun onDeleteFolder(observable: FolderObservable) {
        requestDelete.value = observable.id
    }

    fun setEditObservables(observable: List<FolderObservable>) {
        foldersSelectedLiveData.change(observable)
    }

    fun onEditFolder(observable: FolderObservable) {
        requestEdit.value = Pair(observable.id, newFolderName)
    }

    fun saveFolders() {
        requestSave.value = foldersSelectedLiveData.getList().mapIndexed { index, observable ->
            Pair(observable.id, index)
        }
    }

    override fun getLiveResponses() = groupLiveResponses(
            responseDeleteFolder, responseEditFolder, responseReorderFolder
    )

    companion object {
        const val ACTION_DISMISS = 12211

        private const val TAG = "FOLDER_VIEW_MODEL"
    }
}