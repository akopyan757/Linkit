package com.akopyan757.urlparser

import com.google.gson.annotations.SerializedName

interface IPatternData {
    @SerializedName("pattern")
    fun pattern(): String
    @SerializedName("base")
    fun isBase(): Boolean
    @SerializedName("title_element")
    fun titleElement(): String?
    @SerializedName("description_element")
    fun descriptionElement(): String?
    @SerializedName("image_url_element")
    fun imageUrlElement(): String?
}