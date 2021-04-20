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
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<DeleteUrlLinkUseCase.Params>(schedulerProvider, compositeDisposable) {

    override fun launch(): Completable {
        return remoteDataSource.deleteUrlLink(parameters.linkId)
            .doOnComplete {
                localDataSource.removeUrlLinkById(parameters.linkId)
            }
    }

    data class Params(val linkId: String): UseCase.Params()
}