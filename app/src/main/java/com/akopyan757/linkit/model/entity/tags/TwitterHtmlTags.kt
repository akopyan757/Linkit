package com.akopyan757.linkit.model.entity.tags

data class TwitterHtmlTags(
    val title: String?,
    val url: String?,
    val description: String?,
    val image: String?,
    val creator: String?,
    val site: String?,
    val card: String?
) {
    companion object {
        const val TAG_TWITTER = "twitter:"

        const val TAG_TITLE = "twitter:title"
        const val TAG_URL = "twitter:url"
        const val TAG_DESCRIPTION = "twitter:description"
        const val TAG_IMAGE = "twitter:image"
        const val TAG_CREATOR = "twitter:creator"
        const val TAG_SITE = "twitter:site"
        const val TAG_CARD = "twitter:card"

        fun fromMap(contentMap: Map<String, String>) = TwitterHtmlTags(
                contentMap[TAG_TITLE],
                contentMap[TAG_URL],
                contentMap[TAG_DESCRIPTION],
                contentMap[TAG_IMAGE],
                contentMap[TAG_CREATOR],
                contentMap[TAG_SITE],
                contentMap[TAG_CARD]
        )
    }
}