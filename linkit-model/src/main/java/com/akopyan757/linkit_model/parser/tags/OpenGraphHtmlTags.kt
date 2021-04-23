package com.akopyan757.linkit_model.parser.tags

data class OpenGraphHtmlTags(
    val title: String?,
    val siteName: String?,
    val description: String?,
    val url: String?,
    val type: String?,
    val image: String?,
    val imageWidth: String?,
    val imageHeight: String?,
    val articleAuthor: String?,
    val articlePublishedTime: String?,
    val articleModifiedTime: String?,
    val articleUpdatedTime: String?,
    val locale: String?,
    val priceAmount: String?,
    val priceCurrency: String?,
    val fbAppId: String?,
    val fbAdmins: String?
) {

    fun rating(): Int {
        return title.toOne() + siteName.toOne() + description.toOne() + type.toOne() +
                image.toOne() + articleAuthor.toOne() + articlePublishedTime.toOne() +
                locale.toOne() + priceAmount.toOne() + priceCurrency.toOne()
    }

    private fun String?.toOne() = if (this != null) 1 else 0

    companion object {
        const val TAG_OPEN_GRAPH = "og:"
        const val TAG_ARTICLE = "article:"
        const val TAG_FACEBOOK = "fb:"

        private const val TAG_TITLE = "og:title"
        private const val TAG_SITE_NAME = "og:site_name"
        private const val TAG_URL = "og:url"
        private const val TAG_DESCRIPTION = "og:description"
        private const val TAG_IMAGE = "og:image"
        private const val TAG_IMAGE_WIDTH = "og:image:width"
        private const val TAG_IMAGE_HEIGHT = "og:image:height"
        private const val TAG_TYPE = "og:type"
        private const val TAG_ARTICLE_AUTHOR = "article:author"
        private const val TAG_ARTICLE_PUBLISHED_TIME = "article:published_time"
        private const val TAG_ARTICLE_MODIFIED_TIME = "article:modified_time"
        private const val TAG_ARTICLE_UPDATED_TIME = "article:updated_type"
        private const val TAG_LOCALE = "og:locale"
        private const val TAG_PRICE_AMOUNT = "og:price:amount"
        private const val TAG_PRICE_CURRENCY = "og:price:currency"
        private const val TAG_FB_APP_ID = "fb:app_id"
        private const val TAG_FB_ADMINS = "fb:admins"

        fun fromMap(content: Map<String, String>) = OpenGraphHtmlTags(
                content[TAG_TITLE],
                content[TAG_SITE_NAME],
                content[TAG_DESCRIPTION],
                content[TAG_URL],
                content[TAG_TYPE],
                content[TAG_IMAGE],
                content[TAG_IMAGE_WIDTH],
                content[TAG_IMAGE_HEIGHT],
                content[TAG_ARTICLE_AUTHOR],
                content[TAG_ARTICLE_PUBLISHED_TIME],
                content[TAG_ARTICLE_MODIFIED_TIME],
                content[TAG_ARTICLE_UPDATED_TIME],
                content[TAG_LOCALE],
                content[TAG_PRICE_AMOUNT],
                content[TAG_PRICE_CURRENCY],
                content[TAG_FB_APP_ID],
                content[TAG_FB_ADMINS]
        )
    }
}