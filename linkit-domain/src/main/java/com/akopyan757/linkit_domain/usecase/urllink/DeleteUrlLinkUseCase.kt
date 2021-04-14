package com.akopyan757.linkit_domain.usecase.urllink

import com.akopyan757.linkit_domain.repository.IRemoteUrlDataSource
import com.akopyan757.linkit_domain.usecase.CompletableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.disposables.CompositeDisposable

class DeleteUrlLinkUseCase(
    private val remoteDataSource: IRemoteUrlDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): CompletableWithParamsUseCase<DeleteUrlLinkUseCase.Params>(schedulerProvider, compositeDisposable) {

    override fun launch() = remoteDataSource.deleteUrlLink(parameters.linkId)

    data class Params(val linkId: String): UseCase.Params()
}