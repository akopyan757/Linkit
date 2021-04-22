package com.akopyan757.linkit_domain.usecase.urllink

import com.akopyan757.linkit_domain.repository.ILocalUrlDataSource
import com.akopyan757.linkit_domain.repository.IRemoteUrlDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class MoveTopLinkUseCase(
    private val remoteDataSource: IRemoteUrlDataSource,
    private val localDataSource: ILocalUrlDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<MoveTopLinkUseCase.Params>(compositeDisposable) {

    override fun launch(): Completable {
        return Completable.create { emitter ->
            if (localDataSource.checkExistUrlLink(parameters.linkId)) {
                emitter.onComplete()
            } else {
                emitter.onError(UrlLinkNotFoundException())
            }
        }.toSingle {
            localDataSource.getNewOrderValue()
        }.flatMap { newOrder ->
            remoteDataSource.setOrderForUrlLink(parameters.linkId, newOrder)
        }.flatMapCompletable { order ->
            localDataSource.updateLinkOrder(parameters.linkId, order)
                .subscribeOn(schedulerProvider.ioThread)
        }
        .subscribeOn(schedulerProvider.ioThread)
        .observeOn(schedulerProvider.mainThread)
    }

    class UrlLinkNotFoundException: Exception("Url link not found")

    data class Params(val linkId: String): UseCase.Params()
}