package com.akopyan757.linkit_domain.usecase.urllink

import com.akopyan757.linkit_domain.entity.HtmlLinkCardEntity
import com.akopyan757.linkit_domain.repository.ILoadHtmlCardsDataSource
import com.akopyan757.linkit_domain.usecase.SchedulerProvider
import com.akopyan757.linkit_domain.usecase.SingleWithParamsUseCase
import com.akopyan757.linkit_domain.usecase.UseCase
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class LoadHtmlCardsUseCase(
    private val loadHtmlCardsDataSource: ILoadHtmlCardsDataSource,
    private val schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable
) : SingleWithParamsUseCase<HtmlLinkCardEntity, LoadHtmlCardsUseCase.Params>(compositeDisposable) {

    override fun launch() = Single.fromCallable {
        loadHtmlCardsDataSource.loadCard(parameters.resourceUrl)
    }
    .subscribeOn(schedulerProvider.ioThread)
    .observeOn(schedulerProvider.mainThread)

    data class Params(val resourceUrl: String): UseCase.Params()
}