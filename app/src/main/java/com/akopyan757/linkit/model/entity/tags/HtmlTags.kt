package com.akopyan757.linkit.model.entity.tags

import com.akopyan757.linkit.common.Config

data class HtmlTags(
    val openGraphHtmlTags: OpenGraphHtmlTags,
    val twitterHtmlTags: TwitterHtmlTags,
    val additionalHtmlTags: AdditionalHtmlTags
) {
    fun getTitle() = openGraphHtmlTags.title ?: twitterHtmlTags.title ?: Config.EMPTY
    fun getDescription() = openGraphHtmlTags.description ?: twitterHtmlTags.description ?: Config.EMPTY
    fun getImage() = openGraphHtmlTags.image ?: twitterHtmlTags.image

    enum class Type{
        OpenGraph, Twitter, Simple
    }
}