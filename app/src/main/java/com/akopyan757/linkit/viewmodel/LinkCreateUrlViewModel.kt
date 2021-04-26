package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.usecase.folder.ListenFoldersChangesUseCase
import com.akopyan757.linkit_domain.usecase.urllink.CreateUrlLinkUseCase
import com.akopyan757.linkit_domain.usecase.urllink.LoadHtmlCardsUseCase
import org.koin.core.KoinComponent

class LinkCreateUrlViewModel(
    @get:Bindable val url: String
): BaseViewModel(), KoinComponent {

    private val createLink: CreateUrlLinkUseCase by injectUseCase()
    private val loadCards: LoadHtmlCardsUseCase by injectUseCase()
    private val listenFolders: ListenFoldersChangesUseCase by injectUseCase()

    @get:Bindable var selectedFolderName: String by DB(DEF_FOLDER_NAME, BR.selectedFolderName)
    @get:Bindable var linkObservable: LinkObservable? by DB(null, BR.linkObservable, BR.linObservableVisible)
    @get:Bindable val linObservableVisible: Boolean get() = linkObservable != null

    private var urlLinkEntity: UrlLinkEntity? = null
    private var foldersList = MutableLiveData<List<FolderObservable>>()

    fun getFolderLiveList(): LiveData<List<String>> {
        return foldersList.map { folders -> folders.map { observable -> observable.name } }
    }

    fun startListenFolder() {
        listenFolders.execute({ folders ->
            val observables = mutableListOf<FolderObservable>()
            observables.add(FolderObservable.getDefault(DEF_FOLDER_NAME))
            folders.forEach { folder -> observables.add(FolderObservable.fromData(folder)) }
            foldersList.value = observables
        })
    }

    fun loadHtmlCards() {
        loadCards.execute(LoadHtmlCardsUseCase.Params(url), { card ->
            urlLinkEntity = card
            linkObservable = LinkObservable.from(card)
        })
    }

    fun createNewLink() {
        val folderId = getSelectedFolderId()
        val entity = urlLinkEntity ?: return
        val params = CreateUrlLinkUseCase.Params(folderId, entity)
        createLink.execute(params, onSuccess = { emitAction(ACTION_DISMISS) })
    }

    private fun getSelectedFolderId(): String? {
        val folder = foldersList.value?.find { folder -> folder.name == selectedFolderName }
        return folder?.id?.takeUnless { id -> id == FolderObservable.DEF_FOLDER_ID }
    }

    companion object {
        const val ACTION_DISMISS = 121_1

        private const val DEF_FOLDER_NAME = "Not selected"
    }
}