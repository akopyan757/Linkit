package com.akopyan757.linkit_model.parser.tags


data class AdditionalHtmlTags(
    val canonical: String?,
    val title: String,
    val metaDescription: String?,
    val metaAuthor: String?
) {
    fun rating(): Int {
        return canonical.toOne() + title.toOne() + metaDescription.toOne() + metaAuthor.toOne()
    }

    private fun String?.toOne() = if (this != null) 1 else 0

    companion object {
        const val TAG_CANONICAL = "canonical"
        const val TAG_META_DESCRIPTION = "author"
        const val TAG_META_AUTHOR = "description"
    }
}