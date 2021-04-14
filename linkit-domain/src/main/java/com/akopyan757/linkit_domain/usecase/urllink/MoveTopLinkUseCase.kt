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
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<MoveTopLinkUseCase.Params>(schedulerProvider, compositeDisposable) {

    override fun launch(): Completable {
        return Completable.create { emitter ->
            if (localDataSource.checkExistUrlLink(parameters.linkId)) {
                emitter.onComplete()
            } else {
                emitter.onError(UrlLinkNotFoundException())
            }
        }.toSingle {
            localDataSource.getNewOrderValue()
        }.flatMapCompletable { order ->
            remoteDataSource.setOrderForUrlLink(parameters.linkId, order)
        }
    }

    class UrlLinkNotFoundException: Exception("Url link not found")

    data class Params(val linkId: String): UseCase.Params()
}