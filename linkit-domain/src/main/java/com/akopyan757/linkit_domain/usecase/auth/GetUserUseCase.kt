package com.akopyan757.linkit_domain.usecase.auth

import com.akopyan757.linkit_domain.entity.UserEntity
import com.akopyan757.linkit_domain.repository.IAuthDataSource
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.SingleUseCase
import io.reactivex.disposables.CompositeDisposable

class GetUserUseCase(
    private val authDataSource: IAuthDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): SingleUseCase<UserEntity>(schedulerProvider, compositeDisposable) {

    override fun launch() = authDataSource.getUser()
}