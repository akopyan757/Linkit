package com.akopyan757.linkit_domain

import com.akopyan757.linkit_domain.repository.IFolderRepository
import com.akopyan757.linkit_domain.usecase.CompletableUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class ReorderFolderUseCase(
    private val folderRepository: IFolderRepository,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableUseCase<ReorderFolderUseCase.Params>(schedulerProvider, compositeDisposable) {

    override fun launch() = Completable.fromAction {
        folderRepository.reorderFolders(parameters.folderIds)
    }

    data class Params(val folderIds: List<String>): UseCase.Params()
}