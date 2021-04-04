package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.model.repository.AuthRepository
import com.akopyan757.linkit.model.repository.FolderRepository
import com.akopyan757.linkit.model.repository.LinkRepository

import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import org.koin.core.KoinComponent
import org.koin.core.inject

class LinkViewModel : BaseViewModel(), KoinComponent {

    private val authRepository: AuthRepository by inject()
    private val linkRepository: LinkRepository by inject()
    private val folderRepository: FolderRepository by inject()

    @get:Bindable var isFoldersEmpty: Boolean by DB(false, BR.foldersEmpty)
    @get:Bindable var profileIconUrl: String? by DB(null, BR.profileIconUrl)
    @get:Bindable var profileIconDefaultRes: Int = R.drawable.ic_user
    @get:Bindable val selectedFolderName = MutableLiveData(DEF_FOLDER_NAME)

    private var folderListData: List<FolderObservable> = emptyList()
    private val urlListData = ListLiveData<DiffItemObservable>()

    fun linkListLive() = urlListData

    fun listenRemoteData() = MediatorLiveData<Unit>().apply {
        addSource(linkRepository.listenRemoteData()) { value = Unit }
        addSource(folderRepository.listenRemoteData()) { value = Unit }
    }

    fun listenFolders(): LiveData<List<FolderObservable>> {
        return folderRepository.listenFolderFromCache().map { folders ->
            val observables = mutableListOf<FolderObservable>()
            observables.add(FolderObservable.getDefault(DEF_FOLDER_NAME))
            folders.forEach { folder ->
                observables.add(FolderObservable.fromData(folder))
            }
            folderListData = observables.toList()
            folderListData
        }
    }

    fun listenLinks(): LiveData<Unit> = selectedFolderName.switchMap { folderName ->
        val selectedFolderId = findFolderIdByName(folderName)
        return@switchMap linkRepository.listenUrlDataFromCache(selectedFolderId).handleLiveList(
            viewModel = this,
            onSuccess = { links ->
                val observables = links.map { link -> LinkObservable.from(link) }
                urlListData.change(observables)
            }
        )
    }

    fun requestGetUserAvatar() = requestConvert(
        request = authRepository.getUser(),
        onSuccess = { firebaseUser ->
            profileIconUrl = firebaseUser.photoUrl.toString()
        }
    )

    fun requestDeleteItem(observable: LinkObservable) = requestConvert<Unit, Unit>(
        request = linkRepository.deleteItem(observable.id)
    )

    fun requestMoveLinkToTop(observable: LinkObservable) {
        linkRepository.moveLinkToTop(observable.id)
    }

    fun closeAdItem(observable: DiffItemObservable) {
        urlListData.deleteItem(observable)
    }

    private fun findFolderIdByName(name: String): String? {
        val observable = folderListData.firstOrNull { folder -> folder.name == name }
        return observable?.id?.takeUnless { id -> id == FolderObservable.DEF_FOLDER_ID }
    }

    companion object {
        private const val DEF_FOLDER_NAME = "All folders"
    }
}