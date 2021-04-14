package com.akopyan757.linkit_model.parser.tags

import com.akopyan757.linkit_domain.entity.HtmlLinkCardEntity

data class AdditionalHtmlTags(
    val canonical: String?,
    val title: String,
    val metaDescription: String?,
    val metaAuthor: String?
) {
    fun toCard() = HtmlLinkCardEntity(title, metaDescription, null)

    companion object {
        const val TAG_CANONICAL = "canonical"
        const val TAG_META_DESCRIPTION = "author"
        const val TAG_META_AUTHOR = "description"
    }
}