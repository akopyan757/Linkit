package com.akopyan757.linkit_domain.usecase

import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

abstract class SingleUseCase<T>(
    private val schedulerProvider: SchedulerProvider,
    private val compositeDisposable: CompositeDisposable
): UseCase<Single<T>, SingleUseCase.NothingParams>() {

    fun execute(onSuccess: ((T) -> Unit)? = null, onError: ((Throwable) -> Unit)? = null) {
        val disposable = super.execute(NothingParams)
            .subscribeOn(schedulerProvider.ioThread)
            .observeOn(schedulerProvider.mainThread)
            .subscribe(
                onSuccess ?: defaultOnNextWithParams<T>(),
                onError ?: defaultOnError()
            )

        compositeDisposable.add(disposable)
    }

    object NothingParams: Params()
}