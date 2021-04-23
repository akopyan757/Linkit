package com.akopyan757.linkit_model.datasource

import com.akopyan757.linkit_domain.entity.UrlLinkAppEntity
import com.akopyan757.linkit_domain.entity.UrlLinkEntity
import com.akopyan757.linkit_domain.repository.ILoadHtmlCardsDataSource
import com.akopyan757.linkit_model.parser.IHtmlParser


class LoadHtmlCardsDataSource(
    private val htmlParser: IHtmlParser
): ILoadHtmlCardsDataSource {

    override fun loadCard(resourceUrl: String): UrlLinkEntity {
        val htmlTags = htmlParser.parseHeadTagsFromResource(resourceUrl)
        val appEntity = UrlLinkAppEntity(
            htmlTags.twitterHtmlTags.googlePlayAppId,
            htmlTags.twitterHtmlTags.googlePlayAppName,
            htmlTags.twitterHtmlTags.googlePlayAppUrl,
        )
        val twitterRating = htmlTags.twitterHtmlTags.rating()
        val openGraphRating = htmlTags.openGraphHtmlTags.rating()
        val additionalRating = htmlTags.additionalHtmlTags.rating()
        return if (twitterRating > openGraphRating && twitterRating > additionalRating) {
            val tags = htmlTags.twitterHtmlTags
            val title = tags.title ?: EMPTY_STRING
            val description = tags.description ?: EMPTY_STRING
            val type = when(tags.card) {
                TWITTER_SUMMARY_LARGE -> UrlLinkEntity.Type.LARGE_CARD
                TWITTER_PLAYER -> UrlLinkEntity.Type.PLAYER
                else -> UrlLinkEntity.Type.DEFAULT
            }
            UrlLinkEntity(
                EMPTY_STRING, resourceUrl, title, description, tags.image, null, tags.site,
                type, appEntity, 0
            )
        } else if (openGraphRating > additionalRating) {
            val tags = htmlTags.openGraphHtmlTags
            val title = tags.title ?: EMPTY_STRING
            val description = tags.description ?: EMPTY_STRING
            val image = tags.image
            val type: UrlLinkEntity.Type = UrlLinkEntity.Type.DEFAULT
            UrlLinkEntity(
                EMPTY_STRING, resourceUrl, title, description, image, null, null,
                type, appEntity, 0
            )
        } else {
            val tags = htmlTags.additionalHtmlTags
            val title = tags.title
            val description = tags.metaDescription ?: EMPTY_STRING
            UrlLinkEntity(
                EMPTY_STRING, resourceUrl, title, description, null, null,
                null, UrlLinkEntity.Type.DEFAULT, appEntity, 0
            )
        }
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val TWITTER_SUMMARY_LARGE = "summary_large_image"
        private const val TWITTER_PLAYER = "player"
    }
}