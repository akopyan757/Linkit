package com.akopyan757.linkit_domain.usecase

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

abstract class ObservableUseCase<T>(
    protected val schedulerProvider: SchedulerProvider,
    private val compositeDisposable: CompositeDisposable
): UseCase<Observable<T>, ObservableUseCase.NothingParams>() {

    fun execute(onNext: ((T) -> Unit)? = null, onError: ((Throwable) -> Unit)? = null) {
        val disposable = super.execute(NothingParams)
            .subscribeOn(schedulerProvider.ioThread)
            .observeOn(schedulerProvider.mainThread)
            .subscribe(
                onNext ?: defaultOnNextWithParams<T>(),
                onError ?: defaultOnError()
            )

        compositeDisposable.add(disposable)
    }

    object NothingParams: Params()
}