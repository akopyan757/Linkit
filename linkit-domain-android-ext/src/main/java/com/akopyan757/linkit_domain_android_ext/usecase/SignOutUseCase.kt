package com.akopyan757.linkit_domain_android_ext.usecase

import com.akopyan757.linkit_domain.repository.IAuthDataSource
import com.akopyan757.linkit_domain.usecase.*
import com.akopyan757.linkit_domain_android_ext.datasource.IAuthIntentDataSource
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class SignOutUseCase(
    private val authDataSource: IAuthDataSource,
    private val authIntentService: IAuthIntentDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableUseCase(compositeDisposable) {

    override fun launch(): Completable {
        return authDataSource.signOut()
            .andThen(authIntentService.signOut())
            .subscribeOn(schedulerProvider.ioThread)
            .observeOn(schedulerProvider.mainThread)
    }
}