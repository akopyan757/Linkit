package com.akopyan757.linkit_domain.usecase

import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

abstract class CompletableUseCase(
    private val schedulerProvider: SchedulerProvider,
    private val compositeDisposable: CompositeDisposable
): UseCase<Completable, CompletableUseCase.NothingParams>() {

    fun execute(onSuccess: (() -> Unit)? = null, onError: ((Throwable) -> Unit)? = null) {
        val disposable = super.execute(NothingParams)
            .subscribeOn(schedulerProvider.ioThread)
            .observeOn(schedulerProvider.mainThread)
            .subscribe(
                onSuccess ?: defaultOnNext(),
                onError ?: defaultOnError()
            )

        compositeDisposable.add(disposable)
    }

    object NothingParams: UseCase.Params()
}