package com.akopyan757.linkit.model.parser.tags

data class AdditionalHtmlTags(
    val canonical: String?,
    val title: String,
    val metaDescription: String?,
    val metaAuthor: String?
) {
    companion object {
        const val TAG_CANONICAL = "canonical"
        const val TAG_META_DESCRIPTION = "author"
        const val TAG_META_AUTHOR = "description"
    }
}