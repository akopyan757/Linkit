package com.akopyan757.linkit.viewmodel

import androidx.databinding.Bindable
import com.akopyan757.base.viewmodel.BaseViewModel
import com.akopyan757.base.viewmodel.list.ListLiveData
import com.akopyan757.linkit.BR
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.repository.FolderRepository
import com.akopyan757.linkit.model.repository.LinkRepository

import com.akopyan757.linkit.viewmodel.observable.FolderObservable
import com.akopyan757.linkit.viewmodel.observable.LinkObservable
import org.koin.core.KoinComponent
import org.koin.core.inject

class LinkCreateUrlViewModel(
    @get:Bindable val url: String
): BaseViewModel(), KoinComponent {

    private val folderRepository: FolderRepository by inject()
    private val linkRepository: LinkRepository by inject()

    @get:Bindable var selectedFolderName: String by DB("", BR.selectedFolderName)

    private var foldersList: List<FolderObservable> = emptyList()
    private val cardsList = ListLiveData<LinkObservable>()

    fun getCardsList() = cardsList

    fun loadFolder() = requestConvert(
        request = folderRepository.getFoldersFromCache(),
        onSuccess = { folders ->
            val observables = mutableListOf<FolderObservable>()
            observables.add(FolderObservable.getDefault(DEF_FOLDER_NAME))
            folders.forEach { folder ->
                observables.add(FolderObservable.fromData(folder))
            }
            foldersList = observables
            observables.toList()
        }
    )

    fun requestLoadCards() = requestConvert(
        request = linkRepository.loadAllCards(url),
        onSuccess = { cards ->
            val observables = cards.mapIndexed { index, card ->
                val title = card.title ?: Config.EMPTY
                val description = card.description ?: Config.EMPTY
                LinkObservable(index.toString(), url, title, description, card.photoUrl, false)
            }
            cardsList.change(observables)
        }
    )

    fun requestCreateNewLink() = requestConvert(
        request = linkRepository.createLink(url, getSelectedFolderId()),
        onSuccess = { emitAction(ACTION_DISMISS) },
    )

    private fun getSelectedFolderId(): String? {
        val folder = foldersList.find { folder -> folder.name == selectedFolderName }
        return folder?.id?.takeUnless { id -> id == FolderObservable.DEF_FOLDER_ID }
    }

    companion object {
        const val ACTION_DISMISS = 121_1

        private const val DEF_FOLDER_NAME = "Not selected"
    }
}