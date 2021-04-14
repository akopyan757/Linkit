package com.akopyan757.linkit_domain.usecase.urllink

import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.repository.IExtraLoadUrlDataSource
import com.akopyan757.linkit_domain.repository.ILocalUrlDataSource
import com.akopyan757.linkit_domain.repository.IRemoteUrlDataSource
import com.akopyan757.linkit_domain.usecase.ObservableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class CreateUrlLinkUseCase(
    private val remoteDataSource: IRemoteUrlDataSource,
    private val localDataSource: ILocalUrlDataSource,
    private val extraLoadUrlDataSource: IExtraLoadUrlDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): ObservableWithParamsUseCase<UrlLinkEntity, CreateUrlLinkUseCase.Params>(schedulerProvider, compositeDisposable) {

    override fun launch(): Observable<UrlLinkEntity> {
        return Observable.fromCallable {
            localDataSource.createUrlLinkInstance(parameters.url, parameters.folderId)
        }.flatMap { entity ->
            remoteDataSource.createOrUpdateUrlLink(entity)
        }.flatMap { firstEntity ->
            Observable.create { emitter ->
                emitter.onNext(firstEntity)
                val secondEntity = extraLoadUrlDataSource.loadExtraData(firstEntity)
                emitter.onNext(secondEntity)
                emitter.onComplete()
            }
        }
    }

    data class Params(val url: String, val folderId: String?): UseCase.Params()
}