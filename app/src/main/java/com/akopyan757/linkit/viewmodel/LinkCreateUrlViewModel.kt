package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import com.akopyan757.linkit_domain.entity.HtmlLinkCardEntity
import com.akopyan757.linkit_domain.usecase.folder.ListenFoldersUseCase
import com.akopyan757.linkit_domain.usecase.urllink.CreateUrlLinkUseCase
import com.akopyan757.linkit_domain.usecase.urllink.LoadHtmlCardsUseCase
import org.koin.core.KoinComponent

class LinkCreateUrlViewModel(
    @get:Bindable val url: String
): BaseViewModel(), KoinComponent {

    private val createLink: CreateUrlLinkUseCase by injectUseCase()
    private val listenFolder: ListenFoldersUseCase by injectUseCase()
    private val loadCards: LoadHtmlCardsUseCase by injectUseCase()

    @get:Bindable var selectedFolderName: String by DB("", BR.selectedFolderName)

    private var foldersList = MutableLiveData<List<FolderObservable>>()
    private val cardsList = ListLiveData<LinkObservable>()

    fun getCardsList() = cardsList

    fun getFolderLiveList(): LiveData<List<String>> {
        return foldersList.map { folders -> folders.map { observable -> observable.name } }
    }

    fun startListenFolder() {
        listenFolder.execute({ folders ->
            val observables = mutableListOf<FolderObservable>()
            observables.add(FolderObservable.getDefault(DEF_FOLDER_NAME))
            folders.forEach { folder -> observables.add(FolderObservable.fromData(folder)) }
            foldersList.value = observables
        })
    }

    fun loadHtmlCards() {
        loadCards.execute(LoadHtmlCardsUseCase.Params(url), { cards ->
            cardsList.change(cards.mapIndexed(this::cardToObservable))
        })
    }

    fun createNewLink() {
        val params = CreateUrlLinkUseCase.Params(url, getSelectedFolderId())
        createLink.execute(params, { emitAction(ACTION_DISMISS) })
    }

    private fun getSelectedFolderId(): String? {
        val folder = foldersList.value?.find { folder -> folder.name == selectedFolderName }
        return folder?.id?.takeUnless { id -> id == FolderObservable.DEF_FOLDER_ID }
    }

    private fun cardToObservable(index: Int, card: HtmlLinkCardEntity): LinkObservable {
        val title = card.title ?: Config.EMPTY
        val description = card.description ?: Config.EMPTY
        return LinkObservable(
            index.toString(), url, title, description, card.photoUrl, false
        )
    }

    companion object {
        const val ACTION_DISMISS = 121_1

        private const val DEF_FOLDER_NAME = "Not selected"
    }
}