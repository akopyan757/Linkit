package com.akopyan757.linkit_domain.usecase.folder

import com.akopyan757.linkit_domain.repository.ILocalFolderDataSource
import com.akopyan757.linkit_domain.repository.IRemoteFolderDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class ReorderFolderUseCase(
    private val remoteDataSource: IRemoteFolderDataSource,
    private val localDataSource: ILocalFolderDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<ReorderFolderUseCase.Params>(schedulerProvider, compositeDisposable) {

    override fun launch(): Completable {
        return remoteDataSource.reorderFolders(parameters.folderIds)
            .andThen(localDataSource.updateFoldersOrder(parameters.folderIds))
    }

    data class Params(val folderIds: List<String>): UseCase.Params()
}