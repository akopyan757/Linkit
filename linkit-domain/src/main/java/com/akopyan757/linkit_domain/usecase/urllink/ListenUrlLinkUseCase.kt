package com.akopyan757.linkit_domain.usecase.urllink

import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.repository.ILocalUrlDataSource
import com.akopyan757.linkit_domain.usecase.ObservableWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class ListenUrlLinkUseCase(
    private val localDataSource: ILocalUrlDataSource,
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
): ObservableWithParamsUseCase<List<UrlLinkEntity>, ListenUrlLinkUseCase.Params>(schedulerProvider, compositeDisposable) {

    override fun launch(): Observable<List<UrlLinkEntity>> {
        val folderId = parameters.folderId
        return if (folderId != null) {
            localDataSource.listenUrlLinkFromFolder(folderId)
        } else {
            localDataSource.listenUrlLink()
        }
    }

    fun disposeLastExecute() {
        val disposable = lastDisposable
        if (disposable != null) {
            compositeDisposable.remove(disposable)
            disposable.dispose()
        }
        lastDisposable = null
    }

    data class Params(val folderId: String?): UseCase.Params()
}