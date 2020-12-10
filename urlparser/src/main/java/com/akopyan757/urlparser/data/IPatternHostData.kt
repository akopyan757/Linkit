package com.akopyan757.urlparser.data

import com.google.gson.annotations.SerializedName

interface IPatternHostData {
    @SerializedName("id")
    fun id(): Int

    @SerializedName("host")
    fun host(): String

    @SerializedName("title_element")
    fun hostTitleElement(): String?

    @SerializedName("description_element")
    fun hostDescriptionElement(): String?

    @SerializedName("image_url_element")
    fun hostImageUrlElement(): String?
}