package com.akopyan757.linkit_domain.usecase.urllink

import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.repository.ILocalUrlDataSource
import com.akopyan757.linkit_domain.repository.IRemoteUrlDataSource
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.SingleWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class CreateUrlLinkUseCase(
    private val remoteDataSource: IRemoteUrlDataSource,
    private val localDataSource: ILocalUrlDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): SingleWithParamsUseCase<UrlLinkEntity, CreateUrlLinkUseCase.Params>(schedulerProvider, compositeDisposable) {

    override fun launch(): Single<UrlLinkEntity> {
        return Single.fromCallable {
            localDataSource.createUrlLinkInstance(parameters.url, parameters.folderId)
        }.flatMap { entity ->
            remoteDataSource.createOrUpdateUrlLink(entity)
        }.doOnSuccess { urlEntity ->
            localDataSource.updateUrlLink(urlEntity)
        }
    }

    data class Params(val url: String, val folderId: String?): UseCase.Params()
}