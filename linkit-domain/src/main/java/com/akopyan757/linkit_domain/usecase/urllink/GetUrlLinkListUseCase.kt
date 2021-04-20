package com.akopyan757.linkit_domain.usecase.urllink

import com.akopyan757.linkit_domain.repository.ILocalUrlDataSource
import com.akopyan757.linkit_domain.repository.IRemoteUrlDataSource
import com.akopyan757.linkit_domain.usecase.CompletableUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class GetUrlLinkListUseCase(
    private val remoteDataSource: IRemoteUrlDataSource,
    private val localDataSource: ILocalUrlDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableUseCase(schedulerProvider, compositeDisposable) {

    override fun launch(): Completable {
        return remoteDataSource.loadUrlLinks()
            .flatMapCompletable { urlLinkList ->
                localDataSource.updateAllUrlLinks(urlLinkList)
                    .subscribeOn(schedulerProvider.ioThread)
            }
    }
}