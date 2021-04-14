package com.akopyan757.linkit_domain.usecase.folder

import com.akopyan757.linkit_domain.repository.IRemoteFolderDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class DeleteFolderUseCase(
    private val remoteDataSource: IRemoteFolderDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<DeleteFolderUseCase.Params>(schedulerProvider, compositeDisposable) {

    override fun launch() = Completable.fromAction {
        remoteDataSource.deleteFolder(parameters.folderId)
    }

    data class Params(val folderId: String): UseCase.Params()
}