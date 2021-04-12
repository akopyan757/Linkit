package com.akopyan757.linkit_domain

import com.akopyan757.linkit_domain.repository.IFolderRepository
import com.akopyan757.linkit_domain.usecase.CompletableUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class CreateFolderUseCase(
    private val folderRepository: IFolderRepository,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableUseCase<CreateFolderUseCase.Params>(schedulerProvider, compositeDisposable) {

    override fun launch() = Completable.fromAction {
        folderRepository.createFolder(parameters.name)
    }

    data class Params(val name: String): UseCase.Params()
}