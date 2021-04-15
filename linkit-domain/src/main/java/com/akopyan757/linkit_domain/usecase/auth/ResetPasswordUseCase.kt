package com.akopyan757.linkit_domain.usecase.auth

import com.akopyan757.linkit_domain.repository.IAuthDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class ResetPasswordUseCase(
    private val authDataSource: IAuthDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<ResetPasswordUseCase.Params>(
    schedulerProvider, compositeDisposable
) {

    override fun launch() = Completable.fromAction {
        authDataSource.resetPassword(parameters.email)
    }

    data class Params(val email: String): UseCase.Params()
}