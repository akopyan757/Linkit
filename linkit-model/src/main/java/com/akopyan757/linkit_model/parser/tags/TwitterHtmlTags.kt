package com.akopyan757.linkit_model.parser.tags

import com.akopyan757.linkit_domain.entity.HtmlLinkCardEntity

data class TwitterHtmlTags(
    val title: String?,
    val url: String?,
    val description: String?,
    val image: String?,
    val imageAlt: String?,
    val creator: String?,
    val site: String?,
    val card: String?
) {

    fun toCard() = HtmlLinkCardEntity(title, description, image, site, card)

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

        fun fromMap(contentMap: Map<String, String>) = TwitterHtmlTags(
                contentMap[TAG_TITLE],
                contentMap[TAG_URL],
                contentMap[TAG_DESCRIPTION],
                contentMap[TAG_IMAGE],
                contentMap[TAG_IMAGE_ALT],
                contentMap[TAG_CREATOR],
                contentMap[TAG_SITE],
                contentMap[TAG_CARD]
        )
    }
}