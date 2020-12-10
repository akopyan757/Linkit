package com.akopyan757.urlparser.data

import com.google.gson.annotations.SerializedName

interface IPatternSpecifiedData {

    @SerializedName("id")
    fun specId(): Int

    @SerializedName("host_id")
    fun specHostId(): Int

    @SerializedName("pattern")
    fun specPattern(): String

    @SerializedName("title_element")
    fun specTitleElement(): String?

    @SerializedName("description_element")
    fun specDescriptionElement(): String?

    @SerializedName("image_url_element")
    fun specImageUrlElement(): String?
}