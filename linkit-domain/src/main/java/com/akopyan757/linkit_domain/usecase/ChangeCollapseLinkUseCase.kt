package com.akopyan757.linkit_domain.usecase

import com.akopyan757.linkit_domain.repository.ILocalUrlDataSource
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class ChangeCollapseLinkUseCase(
    private val localDataSource: ILocalUrlDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<ChangeCollapseLinkUseCase.Params>(compositeDisposable) {

    override fun launch(): Completable {
        return localDataSource.changeUrlCollapse(parameters.linkId, parameters.collapse)
                .subscribeOn(schedulerProvider.ioThread)
                .observeOn(schedulerProvider.mainThread)
    }

    data class Params(val linkId: String, val collapse: Boolean): UseCase.Params()
}