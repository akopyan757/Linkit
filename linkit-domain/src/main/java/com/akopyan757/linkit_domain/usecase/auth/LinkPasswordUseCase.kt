package com.akopyan757.linkit_domain.usecase.auth

import com.akopyan757.linkit_domain.entity.UserEntity
import com.akopyan757.linkit_domain.repository.IAuthDataSource
import com.akopyan757.linkit_domain.repository.ILocalFolderDataSource
import com.akopyan757.linkit_domain.repository.IRemoteFolderDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.SingleWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class LinkPasswordUseCase(
    private val authDataSource: IAuthDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): SingleWithParamsUseCase<UserEntity, LinkPasswordUseCase.Params>(compositeDisposable) {

    override fun launch(): Single<UserEntity> {
        return authDataSource.reauthenticateCustomToken()
            .andThen(authDataSource.linkPasswordToAccount(parameters.password))
            .subscribeOn(schedulerProvider.ioThread)
            .observeOn(schedulerProvider.mainThread)
    }

    data class Params(val password: String): UseCase.Params()
}