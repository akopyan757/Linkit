package com.akopyan757.linkit_domain.usecase.urllink

import com.akopyan757.linkit_domain.repository.ILocalUrlDataSource
import com.akopyan757.linkit_domain.repository.IRemoteUrlDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class DeleteUrlLinkUseCase(
    private val remoteDataSource: IRemoteUrlDataSource,
    private val localDataSource: ILocalUrlDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<DeleteUrlLinkUseCase.Params>(compositeDisposable) {

    override fun launch(): Completable {
        return remoteDataSource.deleteUrlLink(parameters.linkId)
            .andThen(
                localDataSource.removeUrlLinkById(parameters.linkId)
                    .subscribeOn(schedulerProvider.ioThread)
            )
            .subscribeOn(schedulerProvider.ioThread)
            .observeOn(schedulerProvider.mainThread)
    }

    data class Params(val linkId: String): UseCase.Params()
}