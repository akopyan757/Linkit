package com.akopyan757.linkit_domain.usecase.folder

import com.akopyan757.linkit_domain.repository.ILocalFolderDataSource
import com.akopyan757.linkit_domain.repository.IRemoteFolderDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class RenameFolderUseCase(
    private val remoteDataSource: IRemoteFolderDataSource,
    private val localDataSource: ILocalFolderDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<RenameFolderUseCase.Params>(compositeDisposable) {

    override fun launch(): Completable = Single.fromCallable {
        localDataSource.checkExistFolder(parameters.folderId)
    }.flatMapCompletable { folderExists ->
        if (folderExists) {
            remoteDataSource.setFolderName(parameters.folderId, parameters.name)
        } else {
            Completable.error(FolderNotFoundException())
        }
    }.andThen(
        localDataSource.updateFolderName(parameters.folderId, parameters.name)
            .subscribeOn(schedulerProvider.ioThread)
    )
    .subscribeOn(schedulerProvider.ioThread)
    .observeOn(schedulerProvider.mainThread)

    data class Params(val folderId: String, val name: String): UseCase.Params()

    class FolderNotFoundException: Exception("Folder not found")
}