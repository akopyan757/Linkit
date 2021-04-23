package com.akopyan757.linkit_model.parser.tags

import com.akopyan757.linkit_domain.entity.HtmlLinkAppEntity
import com.akopyan757.linkit_domain.entity.HtmlLinkCardEntity

data class TwitterHtmlTags(
    val title: String?,
    val url: String?,
    val description: String?,
    val image: String?,
    val imageAlt: String?,
    val creator: String?,
    val site: String?,
    val card: String?,
    val country: String?,
    val googlePlayAppId: String?,
    val googlePlayAppName: String?,
    val googlePlayAppUrl: String?
) {

    fun toCard() = HtmlLinkCardEntity(title, description, image, site, card)

    fun toAppData(): HtmlLinkAppEntity? {
        val appId = googlePlayAppId ?: return null
        val appName = googlePlayAppName ?: return null
        val appUrl = googlePlayAppUrl ?: return null
        return HtmlLinkAppEntity(appId, appName, appUrl)
    }

    companion object {
        const val TAG_TWITTER = "twitter:"

        private const val TAG_TITLE = "twitter:title"
        private const val TAG_URL = "twitter:url"
        private const val TAG_DESCRIPTION = "twitter:description"
        private const val TAG_IMAGE = "twitter:image"
        private const val TAG_IMAGE_ALT = "twitter:image:alt"
        private const val TAG_CREATOR = "twitter:creator"
        private const val TAG_SITE = "twitter:site"
        private const val TAG_CARD = "twitter:card"
        private const val TAG_COUNTRY = "twitter:country"
        private const val TAG_GOOGLE_PLAY_APP_ID = "twitter:app:id:googleplay"
        private const val TAG_GOOGLE_PLAY_APP_NAME = "twitter:app:name:googleplay"
        private const val TAG_GOOGLE_PLAY_APP_URL = "twitter:app:url:googleplay"

        fun fromMap(contentMap: Map<String, String>) = TwitterHtmlTags(
                contentMap[TAG_TITLE],
                contentMap[TAG_URL],
                contentMap[TAG_DESCRIPTION],
                contentMap[TAG_IMAGE],
                contentMap[TAG_IMAGE_ALT],
                contentMap[TAG_CREATOR],
                contentMap[TAG_SITE],
                contentMap[TAG_CARD],
                contentMap[TAG_COUNTRY],
                contentMap[TAG_GOOGLE_PLAY_APP_ID],
                contentMap[TAG_GOOGLE_PLAY_APP_NAME],
                contentMap[TAG_GOOGLE_PLAY_APP_URL],
            )
    }
}