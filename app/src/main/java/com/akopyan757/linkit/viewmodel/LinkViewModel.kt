package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.DiffItemObservable
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.R
import com.akopyan757.linkit.model.repository.AuthRepository
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import com.akopyan757.linkit_domain.usecase.folder.ListenFolderChangesUseCase
import com.akopyan757.linkit_domain.usecase.folder.ListenFoldersUseCase
import com.akopyan757.linkit_domain.usecase.urllink.DeleteUrlLinkUseCase
import com.akopyan757.linkit_domain.usecase.urllink.ListenUrlLinkChangesUseCase
import com.akopyan757.linkit_domain.usecase.urllink.ListenUrlLinkUseCase
import com.akopyan757.linkit_domain.usecase.urllink.MoveTopLinkUseCase
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

class LinkViewModel : BaseViewModel(), KoinComponent {

    private val authRepository: AuthRepository by inject()
    private val listenFolder: ListenFoldersUseCase by injectUseCase()
    private val listenUrlLinks: ListenUrlLinkUseCase by injectUseCase()
    private val deleteUrlLink: DeleteUrlLinkUseCase by injectUseCase()
    private val moveTopLink: MoveTopLinkUseCase by injectUseCase()
    private val listenFolderChanges: ListenFolderChangesUseCase by injectUseCase()
    private val listenUrlLinkChanges: ListenUrlLinkChangesUseCase by injectUseCase()

    @get:Bindable var isFoldersEmpty: Boolean by DB(false, BR.foldersEmpty)
    @get:Bindable var profileIconUrl: String? by DB(null, BR.profileIconUrl)
    @get:Bindable var profileIconDefaultRes: Int = R.drawable.ic_user
    @get:Bindable val selectedFolderName = MutableLiveData(DEF_FOLDER_NAME)

    private var folderList = MutableLiveData<List<FolderObservable>>()
    private val urlListData = ListLiveData<DiffItemObservable>()

    fun linkListLive() = urlListData

    fun startListenDataChanges()  {
        listenFolderChanges.execute()
        listenUrlLinkChanges.execute()
    }

    fun listenSelectedFolder(): LiveData<String?> {
        return selectedFolderName.map { folderName -> findFolderIdByName(folderName) }
    }

    fun listenFolderNames(): LiveData<List<String>> {
        return folderList.map { folders -> folders.map { observable -> observable.name } }
    }

    fun startListenFolders() {
        listenFolder.execute({ folders ->
            val observables = mutableListOf<FolderObservable>()
            observables.add(FolderObservable.getDefault(DEF_FOLDER_NAME))
            folders.forEach { folder ->
                observables.add(FolderObservable.fromData(folder))
            }
            folderList.value = observables
        })
    }

    fun listenLinks(selectedFolderIdOrNull: String?) {
        listenUrlLinks.disposeLastExecute()
        listenUrlLinks.execute(ListenUrlLinkUseCase.Params(selectedFolderIdOrNull), { links ->
            val observables = links.map { link -> LinkObservable.from(link) }
            urlListData.change(observables)
        })
    }

    fun requestGetUserAvatar() = requestConvert(
        request = authRepository.getUser(),
        onSuccess = { firebaseUser ->
            profileIconUrl = firebaseUser.photoUrl.toString()
        }
    )

    fun deleteUrlLink(observable: LinkObservable) {
        deleteUrlLink.execute(DeleteUrlLinkUseCase.Params(observable.id))
    }

    fun moveUrlLinkToTop(observable: LinkObservable) {
        moveTopLink.execute(MoveTopLinkUseCase.Params(observable.id))
    }

    fun closeAdItem(observable: DiffItemObservable) {
        urlListData.deleteItem(observable)
    }

    private fun findFolderIdByName(name: String): String? {
        val observable = folderList.value?.firstOrNull { folder -> folder.name == name }
        return observable?.id?.takeUnless { id -> id == FolderObservable.DEF_FOLDER_ID }
    }

    companion object {
        private const val DEF_FOLDER_NAME = "All folders"
    }
}