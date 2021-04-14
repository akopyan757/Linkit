package com.akopyan757.linkit_domain.usecase.folder

import com.akopyan757.linkit_domain.entity.DataChange
import com.akopyan757.linkit_domain.entity.FolderEntity
import com.akopyan757.linkit_domain.repository.ILocalFolderDataSource
import com.akopyan757.linkit_domain.repository.IRemoteFolderDataSource
import com.akopyan757.linkit_domain.usecase.ObservableUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class ListenFoldersChangesUseCase(
    private val remoteDataSource: IRemoteFolderDataSource,
    private val localDataSource: ILocalFolderDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): ObservableUseCase<DataChange<FolderEntity>>(schedulerProvider, compositeDisposable) {

    override fun launch(): Observable<DataChange<FolderEntity>> {
        val firstLoadSingle: Single<DataChange<FolderEntity>> = remoteDataSource.loadFolders()
            .doAfterSuccess { folders -> localDataSource.updateAllFolders(folders) }
            .map { folders -> DataChange.Initialized(folders) }

        val updatesObservables = remoteDataSource.listenFolderChanges()
            .doAfterNext { changes ->
                when(changes) {
                    is DataChange.Added -> localDataSource.updateFolder(changes.data)
                    is DataChange.Modified -> localDataSource.updateFolder(changes.data)
                    is DataChange.Deleted -> localDataSource.removeFolderById(changes.data.id)
                    else -> {}
                }
            }

        return firstLoadSingle.flatMapObservable { initialDataChange ->
            updatesObservables.startWith(initialDataChange)
        }
    }
}