package com.akopyan757.linkit.model.entity

import com.akopyan757.linkit.model.entity.tags.HtmlTags

data class HtmlLinkCard(
    var title: String?,
    var description: String?,
    val photoUrl: String?
) {
    companion object {
        fun getCard(tags: HtmlTags, type: HtmlTags.Type): HtmlLinkCard = when (type) {
            HtmlTags.Type.OpenGraph -> {
                val title = tags.openGraphHtmlTags.title
                val description = tags.openGraphHtmlTags.description
                val photoUrl = tags.openGraphHtmlTags.image
                HtmlLinkCard(title, description, photoUrl)
            }
            HtmlTags.Type.Twitter -> {
                val title = tags.twitterHtmlTags.title
                val description = tags.twitterHtmlTags.description
                val photoUrl = tags.twitterHtmlTags.image
                HtmlLinkCard(title, description, photoUrl)
            }
            HtmlTags.Type.Simple -> {
                val title = tags.additionalHtmlTags.title
                val description = tags.additionalHtmlTags.metaDescription
                HtmlLinkCard(title, description, null)
            }
        }
    }
}