package com.akopyan757.linkit_domain.usecase.folder

import com.akopyan757.linkit_domain.repository.IRemoteFolderDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.disposables.CompositeDisposable

class ReorderFolderUseCase(
    private val remoteDataSource: IRemoteFolderDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<ReorderFolderUseCase.Params>(schedulerProvider, compositeDisposable) {

    override fun launch() = remoteDataSource.reorderFolders(parameters.folderIds)

    data class Params(val folderIds: List<String>): UseCase.Params()
}