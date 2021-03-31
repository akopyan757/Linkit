package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.repository.AuthRepository
import com.akopyan757.linkit.model.repository.LinkRepository
import com.akopyan757.linkit.view.scope.mainInject
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import org.koin.core.KoinComponent
import org.koin.core.inject

class LinkViewModel : BaseViewModel(), KoinComponent {

    private val authRepository: AuthRepository by inject()
    private val linkRepository: LinkRepository by mainInject()

    @get:Bindable var isFoldersEmpty: Boolean by DB(false, BR.foldersEmpty)
    @get:Bindable var profileIconUrl: String? by DB(null, BR.profileIconUrl)
    @get:Bindable var profileIconDefaultRes: Int = R.drawable.ic_user
    @get:Bindable val selectedFolderName = MutableLiveData(FolderData.GENERAL_FOLDER_NAME_TITLE)

    private val folderListData = mutableListOf<FolderObservable>()
    private val urlListData = ListLiveData<DiffItemObservable>()

    fun linkListLive() = urlListData

    fun listenFolders(): LiveData<List<FolderObservable>> {
        return linkRepository.getAllFolders().switchMap { folders ->
            liveData(viewModelScope.coroutineContext) {
                val observables = folders.map { folder ->
                    if (folder.isGeneral()) {
                        FolderObservable(folder.id, FolderData.GENERAL_FOLDER_NAME_TITLE)
                    } else {
                        FolderObservable(folder.id, folder.name)
                    }
                }
                folderListData.addAll(observables)
                emit(observables)
            }
        }
    }

    fun listenLinks(): LiveData<Int> = selectedFolderName.map { folderName ->
        val selectedFolderId = findFolderIdByNameOrDefault(folderName)
        bindLiveList(
            request = linkRepository.getUrlLinksByFolder(selectedFolderId),
            listLiveData = urlListData,
            onMap = { links -> links.map { link -> LinkObservable.from(link) } }
        )
        selectedFolderId
    }

    fun requestFetchRemoteData() = requestConvert(
        request = linkRepository.fetchRemoteData(),
        onSuccess = {}
    )

    fun requestGetUserAvatar() = requestConvert(
        request = authRepository.getUser(),
        onSuccess = { firebaseUser ->
            profileIconUrl = firebaseUser.photoUrl.toString()
        }
    )

    fun requestMoveLinkToTop(observable: LinkObservable) {
        linkRepository.moveLinkToTop(observable.id)
    }

    fun closeAdItem(observable: DiffItemObservable) {
        urlListData.deleteItem(observable)
    }

    private fun findFolderIdByNameOrDefault(name: String): Int {
        return folderListData.firstOrNull { folder -> folder.name == name }?.id
            ?: FolderData.GENERAL_FOLDER_ID
    }
}