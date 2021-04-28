package com.akopyan757.linkit_domain.usecase.urllink

import com.akopyan757.linkit_domain.repository.ILocalUrlDataSource
import com.akopyan757.linkit_domain.repository.IRemoteUrlDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class ChangeCollapseLinkUseCase(
    private val remoteDataSource: IRemoteUrlDataSource,
    private val localDataSource: ILocalUrlDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<ChangeCollapseLinkUseCase.Params>(compositeDisposable) {

    override fun launch(): Completable {
        return remoteDataSource.changeUrlCollapse(parameters.linkId, parameters.collapse)
            .andThen(
                localDataSource.changeUrlCollapse(parameters.linkId, parameters.collapse)
                    .subscribeOn(schedulerProvider.ioThread)
            )
            .subscribeOn(schedulerProvider.ioThread)
            .observeOn(schedulerProvider.mainThread)
    }

    data class Params(val linkId: String, val collapse: Boolean): UseCase.Params()
}