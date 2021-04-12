package com.akopyan757.linkit_domain

import com.akopyan757.linkit_domain.repository.IFolderRepository
import com.akopyan757.linkit_domain.usecase.CompletableUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class RenameFolderUseCase(
    private val folderRepository: IFolderRepository,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableUseCase<RenameFolderUseCase.Params>(schedulerProvider, compositeDisposable) {

    override fun launch() = Completable.fromAction {
        folderRepository.renameFolder(parameters.folderId, parameters.name)
    }

    data class Params(val folderId: String, val name: String): UseCase.Params()
}