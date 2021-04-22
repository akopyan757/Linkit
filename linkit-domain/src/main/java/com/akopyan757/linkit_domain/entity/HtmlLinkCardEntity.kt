package com.akopyan757.linkit_domain.entity

data class HtmlLinkCardEntity(
    var title: String? = null,
    var description: String? = null,
    val photoUrl: String? = null
) {
    fun rating(): Int {
        val titleRating = title?.let { TWO } ?: ZERO
        val descriptionRating = description?.let { ONE } ?: ZERO
        val photoUrlRating = photoUrl?.let { ONE } ?: ZERO
        return titleRating + descriptionRating + photoUrlRating
    }

    companion object {
        private const val TWO = 2
        private const val ONE = 1
        private const val ZERO = 0
    }

}