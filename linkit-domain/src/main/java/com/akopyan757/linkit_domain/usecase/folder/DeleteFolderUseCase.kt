package com.akopyan757.linkit_domain.usecase.folder

import com.akopyan757.linkit_domain.repository.ILocalFolderDataSource
import com.akopyan757.linkit_domain.repository.IRemoteFolderDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class DeleteFolderUseCase(
    private val remoteDataSource: IRemoteFolderDataSource,
    private val localDataSource: ILocalFolderDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<DeleteFolderUseCase.Params>(compositeDisposable) {

    override fun launch(): Completable {
        return remoteDataSource.deleteFolder(parameters.folderId)
            .andThen(
                localDataSource.removeFolderById(parameters.folderId)
                    .subscribeOn(schedulerProvider.ioThread)
            )
            .subscribeOn(schedulerProvider.ioThread)
            .observeOn(schedulerProvider.mainThread)
    }

    data class Params(val folderId: String): UseCase.Params()
}