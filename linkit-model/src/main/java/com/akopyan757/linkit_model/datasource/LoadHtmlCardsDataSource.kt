package com.akopyan757.linkit_model.datasource

import android.util.Log
import com.akopyan757.linkit_domain.entity.HtmlLinkCardEntity
import com.akopyan757.linkit_domain.repository.ILoadHtmlCardsDataSource
import com.akopyan757.linkit_model.parser.IHtmlParser


class LoadHtmlCardsDataSource(
    private val htmlParser: IHtmlParser
): ILoadHtmlCardsDataSource {

    override fun loadCard(resourceUrl: String): HtmlLinkCardEntity {
        val htmlTags = htmlParser.parseHeadTagsFromResource(resourceUrl)
        Log.i("LoadHtmlCardsDataSource", htmlTags.toString())
        return listOf(
            htmlTags.twitterHtmlTags.toCard().apply {
                googleApp = htmlTags.twitterHtmlTags.toAppData()
            },
            htmlTags.openGraphHtmlTags.toCard().apply {
                googleApp = htmlTags.twitterHtmlTags.toAppData()
            },
            htmlTags.additionalHtmlTags.toCard().apply {
                googleApp = htmlTags.twitterHtmlTags.toAppData()
            }
        ).maxByOrNull { card -> card.rating() } ?: HtmlLinkCardEntity()
    }
}