package com.akopyan757.linkit_domain.usecase

import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

abstract class CompletableWithParamsUseCase<P : UseCase.Params>(
    private val schedulerProvider: SchedulerProvider,
    private val compositeDisposable: CompositeDisposable
): UseCase<Completable, P>() {

    fun execute(params: P, onSuccess: (() -> Unit)? = null, onError: ((Throwable) -> Unit)? = null) {
        val disposable = super.execute(params)
            .subscribeOn(schedulerProvider.ioThread)
            .observeOn(schedulerProvider.mainThread)
            .subscribe(
                onSuccess ?: defaultOnNext(),
                onError ?: defaultOnError()
            )

        compositeDisposable.add(disposable)
    }
}