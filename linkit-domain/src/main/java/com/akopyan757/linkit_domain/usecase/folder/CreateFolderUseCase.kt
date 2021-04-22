package com.akopyan757.linkit_domain.usecase.folder

import com.akopyan757.linkit_domain.repository.ILocalFolderDataSource
import com.akopyan757.linkit_domain.repository.IRemoteFolderDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class CreateFolderUseCase(
    private val remoteDataSource: IRemoteFolderDataSource,
    private val localDataSource: ILocalFolderDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<CreateFolderUseCase.Params>(compositeDisposable) {

    override fun launch() = Single.fromCallable {
        localDataSource.createFolderInstance(parameters.name)
    }.flatMap { folderEntity ->
        remoteDataSource.createOrUpdateFolder(folderEntity).toSingle { folderEntity }
    }.flatMapCompletable { folderEntity ->
        localDataSource.insertFolder(folderEntity)
            .subscribeOn(schedulerProvider.ioThread)
    }
    .subscribeOn(schedulerProvider.ioThread)
    .observeOn(schedulerProvider.mainThread)

    data class Params(val name: String): UseCase.Params()
}