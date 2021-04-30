package com.akopyan757.linkit.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.viewmodel.observable.*
import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.usecase.auth.GetUserUseCase
import com.akopyan757.linkit_domain.usecase.folder.ListenFoldersChangesUseCase
import com.akopyan757.linkit_domain.usecase.urllink.*
import org.koin.core.KoinComponent

class LinkViewModel : BaseViewModel(), KoinComponent {

    private val getUser: GetUserUseCase by injectUseCase()
    private val listenUrlLinks: ListenUrlLinkUseCase by injectUseCase()
    private val deleteUrlLink: DeleteUrlLinkUseCase by injectUseCase()
    private val moveTopLink: MoveTopLinkUseCase by injectUseCase()
    private val listenFolders: ListenFoldersChangesUseCase by injectUseCase()
    private val getUrlLinkList: GetUrlLinkListUseCase by injectUseCase()
    private val updateAssignUrlLink: UpdateAssignUrlLinkUseCase by injectUseCase()

    @get:Bindable var isFoldersEmpty: Boolean by DB(false, BR.foldersEmpty)
    @get:Bindable var profileIconUrl: String? by DB(null, BR.profileIconUrl)
    @get:Bindable var profileIconDefaultRes: Int = R.drawable.ic_user

    private val editCheckedCount = MutableLiveData(0)
    private val editMode = editCheckedCount.map { value -> value != 0 }
    private var folderList = MutableLiveData<List<FolderObservable>>()
    private val urlListData = ListLiveData<DiffItemObservable>()

    fun linkListLive() = urlListData

    fun startListenDataChanges()  {
        getUrlLinkList.execute()
    }

    fun listenFolderNames(): LiveData<List<FolderObservable>> = folderList
    fun getFolderObservableList(): List<FolderObservable> {
        return folderList.value?.mapNotNull { observable ->
            if (observable.name != DEF_FOLDER_NAME) observable else null
        } ?: emptyList()
    }

    fun listenCheckedItems(): LiveData<String> = editCheckedCount.map { value -> value.toString() }
    fun listenEditMode(): LiveData<Boolean> = editMode
    fun getEditModeState(): Boolean = editMode.value ?: false

    fun listenNotEditMode(): LiveData<Boolean> = editMode.map { value -> value.not() }

    fun startListenFolders() {
        listenFolders.execute({ folders ->
            val observables = mutableListOf<FolderObservable>()
            observables.add(FolderObservable.getDefault(DEF_FOLDER_NAME))
            folders.forEach { folder ->
                observables.add(FolderObservable.fromData(folder))
            }
            folderList.value = observables
        }, onError = {
            Log.e("LinkViewModel", "listenFolders", it)
        })
        getUrlLinkList.execute(onError = {
            Log.e("LinkViewModel", "getUrlLinkList", it)
        })
    }

    fun listenLinksById(folderId: String?) {
        val id = folderId?.takeUnless { id -> id == FolderObservable.DEF_FOLDER_ID }
        listenUrlLinks.disposeLastExecute()
        listenUrlLinks.execute(ListenUrlLinkUseCase.Params(id), { links ->
            val observables = links.map(LinkObservable::from).toMutableList<DiffItemObservable>()
            (0 until observables.size / REPEAT_AD).forEach { adIndex ->
                val index = adIndex * REPEAT_AD + REPEAT_AD - 1
                observables.add(index, AdObservable(adIndex + 1))
            }
            urlListData.change(observables)
        }, onError = {
            Log.e("LinkViewModel", "listenLinksById", it)
        })
    }

    fun onEditLinkItem(observable: BaseLinkObservable) {
        resetEditMode()
        observable.toggleCheck()
        urlListData.changeItem(observable) {
            changeEditMode()
        }
    }

    fun closeEditMode() {
        val uncheckedObservables = urlListData.getList().map { observable ->
            if (observable is BaseLinkObservable) observable.resetCheck(); observable
        }
        urlListData.change(uncheckedObservables) {
            resetEditMode()
        }
    }

    fun getUserAvatar() {
        getUser.execute(onSuccess = { userEntity ->
            profileIconUrl = userEntity.photoUrl
        })
    }

    fun deleteUrlLinks() {
        val linkIds = getCheckedLinksIds()
        deleteUrlLink.execute(DeleteUrlLinkUseCase.Params(linkIds), onError = {
            Log.e("LinkViewModel", "deleteUrlLink", it)
        })
    }

    fun assignLinksToFolder(folderId: String) {
        val linkIds = getCheckedLinksIds()
        updateAssignUrlLink.execute(UpdateAssignUrlLinkUseCase.Params(folderId, linkIds),
            onSuccess = {
                Log.i("LinkViewModel", "assignLinksToFolder success")
            }, onError = { throwable ->
                Log.e("LinkViewModel", "assignLinksToFolder", throwable)
            })
    }

    fun moveUrlLinkToTop(observable: BaseLinkObservable) {
        moveTopLink.execute(MoveTopLinkUseCase.Params(observable.id),
            onSuccess = {
                Log.i("LinkViewModel", "moveTopLink: success")
            },
            onError = { throwable ->
                Log.e("LinkViewModel", "moveTopLink: error", throwable)
            }
        )
    }

    fun closeAdItem(observable: DiffItemObservable) {
        urlListData.deleteItem(observable)
    }

    fun getCheckedLinksCount(): Int {
        return getCheckedLinksIds().size
    }

    private fun resetEditMode() {
        editCheckedCount.value = 0
    }

    private fun changeEditMode() {
        editCheckedCount.value = urlListData.getList().count { link ->
            if (link is BaseLinkObservable) link.checked else false
        }
    }
    
    private fun getCheckedLinksIds(): List<String> {
        return urlListData.getList().mapNotNull { observable ->
            if (observable is BaseLinkObservable && observable.checked) observable.id else null
        }
    }

    companion object {
        private const val DEF_FOLDER_NAME = "All folders"
        private const val REPEAT_AD = 7
    }
}