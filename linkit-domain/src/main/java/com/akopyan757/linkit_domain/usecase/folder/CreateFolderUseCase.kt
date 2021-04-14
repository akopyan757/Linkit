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
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<CreateFolderUseCase.Params>(schedulerProvider, compositeDisposable) {

    override fun launch() = Single.fromCallable {
        localDataSource.createFolder(parameters.name)
    }.flatMapCompletable { folderEntity ->
        remoteDataSource.createOrUpdateFolder(folderEntity)
    }

    data class Params(val name: String): UseCase.Params()
}