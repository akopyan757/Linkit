package com.akopyan757.linkit_domain.usecase.folder

import com.akopyan757.linkit_domain.entity.FolderEntity
import com.akopyan757.linkit_domain.repository.ILocalFolderDataSource
import com.akopyan757.linkit_domain.repository.IRemoteFolderDataSource
import com.akopyan757.linkit_domain.usecase.ObservableUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.SingleUseCase
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class ListenFoldersChangesUseCase(
    private val localDataSource: ILocalFolderDataSource,
    private val remoteDataSource: IRemoteFolderDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): ObservableUseCase<List<FolderEntity>>(schedulerProvider, compositeDisposable) {

    override fun launch(): Observable<List<FolderEntity>> {
        return remoteDataSource.loadFolders()
            .flatMapCompletable { folders ->
                localDataSource.updateAllFolders(folders)
                    .subscribeOn(schedulerProvider.ioThread)
            }
            .andThen(localDataSource.listenFolders())
    }
}