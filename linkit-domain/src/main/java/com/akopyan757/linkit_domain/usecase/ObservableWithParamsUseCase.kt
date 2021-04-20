package com.akopyan757.linkit_domain.usecase

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class ObservableWithParamsUseCase<T, P : UseCase.Params>(
    protected val schedulerProvider: SchedulerProvider,
    protected val compositeDisposable: CompositeDisposable
): UseCase<Observable<T>, P>() {

    protected var lastDisposable: Disposable? = null

    fun execute(params: P, onNext: ((T) -> Unit)? = null, onError: ((Throwable) -> Unit)? = null) {
        val disposable = super.execute(params)
            .subscribeOn(schedulerProvider.ioThread)
            .observeOn(schedulerProvider.mainThread)
            .subscribe(
                onNext ?: defaultOnNextWithParams<T>(),
                onError ?: defaultOnError()
            )

        lastDisposable = disposable
        compositeDisposable.add(disposable)
    }
}