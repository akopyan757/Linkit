package com.akopyan757.linkit_domain.usecase.urllink

import com.akopyan757.linkit_domain.entity.DataChange
import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.repository.ILocalUrlDataSource
import com.akopyan757.linkit_domain.repository.IRemoteUrlDataSource
import com.akopyan757.linkit_domain.usecase.ObservableUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class ListenUrlLinkChangesUseCase(
    private val remoteDataSource: IRemoteUrlDataSource,
    private val localDataSource: ILocalUrlDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): ObservableUseCase<DataChange<UrlLinkEntity>>(schedulerProvider, compositeDisposable) {

    override fun launch(): Observable<DataChange<UrlLinkEntity>> {
        val firstLoadSingle = remoteDataSource.loadUrlLinks()
            .doOnSuccess { folders -> localDataSource.updateAllUrlLinks(folders) }
            .map { folders -> DataChange.Initialized(folders) }

        val updatesObservables = remoteDataSource.listenUrlLinksChanges()
            .doOnNext { changes ->
                when(changes) {
                    is DataChange.Added -> localDataSource.updateUrlLink(changes.data)
                    is DataChange.Modified -> localDataSource.updateUrlLink(changes.data)
                    is DataChange.Deleted -> localDataSource.removeUrlLinkById(changes.data.id)
                    else -> {}
                }
            }

        return firstLoadSingle.flatMapObservable { initialDataChange ->
            updatesObservables.startWith(initialDataChange)
        }
    }
}