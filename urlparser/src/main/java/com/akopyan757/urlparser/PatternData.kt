package com.akopyan757.urlparser

import com.google.gson.annotations.SerializedName

data class PatternData(
    @SerializedName("pattern")
    val pattern: String,
    @SerializedName("base")
    val base: Boolean,
    @SerializedName("title_element")
    val title_element: String?,
    @SerializedName("description_element")
    val description_element: String?,
    @SerializedName("image_url_element")
    val image_url_element: String?
): IPatternData {
    override fun pattern() = pattern
    override fun isBase() = base
    override fun titleElement() = title_element
    override fun descriptionElement() = description_element
    override fun imageUrlElement() = image_url_element
}