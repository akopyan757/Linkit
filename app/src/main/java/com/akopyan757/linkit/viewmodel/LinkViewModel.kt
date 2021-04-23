package com.akopyan757.linkit.viewmodel

import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.viewmodel.observable.BaseLinkObservable
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.observable.LinkLargeObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.usecase.auth.GetUserUseCase
import com.akopyan757.linkit_domain.usecase.folder.ListenFoldersChangesUseCase
import com.akopyan757.linkit_domain.usecase.urllink.DeleteUrlLinkUseCase
import com.akopyan757.linkit_domain.usecase.urllink.GetUrlLinkListUseCase
import com.akopyan757.linkit_domain.usecase.urllink.ListenUrlLinkUseCase
import com.akopyan757.linkit_domain.usecase.urllink.MoveTopLinkUseCase
import org.koin.core.KoinComponent

class LinkViewModel : BaseViewModel(), KoinComponent {

    private val getUser: GetUserUseCase by injectUseCase()
    private val listenUrlLinks: ListenUrlLinkUseCase by injectUseCase()
    private val deleteUrlLink: DeleteUrlLinkUseCase by injectUseCase()
    private val moveTopLink: MoveTopLinkUseCase by injectUseCase()
    private val listenFolders: ListenFoldersChangesUseCase by injectUseCase()
    private val getUrlLinkList: GetUrlLinkListUseCase by injectUseCase()

    @get:Bindable var isFoldersEmpty: Boolean by DB(false, BR.foldersEmpty)
    @get:Bindable var profileIconUrl: String? by DB(null, BR.profileIconUrl)
    @get:Bindable var profileIconDefaultRes: Int = R.drawable.ic_user

    private var folderList = MutableLiveData<List<FolderObservable>>()
    private val urlListData = ListLiveData<DiffItemObservable>()

    fun linkListLive() = urlListData

    fun startListenDataChanges()  {
        getUrlLinkList.execute()
    }

    fun listenFolderNames(): LiveData<List<FolderObservable>> {
        return folderList
    }

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
            val observables = links.map { link ->
                when (link.type) {
                    UrlLinkEntity.Type.DEFAULT -> LinkObservable.from(link)
                    UrlLinkEntity.Type.LARGE_CARD -> LinkLargeObservable.from(link)
                    UrlLinkEntity.Type.PLAYER -> LinkLargeObservable.from(link)
                }
            }
            urlListData.change(observables)
        }, onError = {
            Log.e("LinkViewModel", "listenLinksById", it)
        })
    }

    fun getUserAvatar() {
        getUser.execute(onSuccess = { userEntity ->
            profileIconUrl = userEntity.photoUrl
        })
    }

    fun deleteUrlLink(observable: BaseLinkObservable) {
        deleteUrlLink.execute(DeleteUrlLinkUseCase.Params(observable.id), onError = {
            Log.e("LinkViewModel", "deleteUrlLink", it)
        })
    }

    fun moveUrlLinkToTop(observable: BaseLinkObservable) {
        moveTopLink.execute(MoveTopLinkUseCase.Params(observable.id))
    }

    fun closeAdItem(observable: DiffItemObservable) {
        urlListData.deleteItem(observable)
    }

    companion object {
        private const val DEF_FOLDER_NAME = "All folders"
    }
}