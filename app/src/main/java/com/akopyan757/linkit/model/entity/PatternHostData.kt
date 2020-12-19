package com.akopyan757.linkit.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.akopyan757.urlparser.data.IPatternHostData
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pattern_host_data")
data class PatternHostData @Ignore constructor(

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @ColumnInfo(name = "host_id")
    var id: Int,

    @SerializedName("host")
    @ColumnInfo(name = "host")
    var host: String = "",

    @SerializedName("title_element")
    @ColumnInfo(name = "host_title_element")
    var titleElement: String?,

    @SerializedName("description_element")
    @ColumnInfo(name = "host_description_element")
    var descriptionElement: String?,

    @SerializedName("image_url_element")
    @ColumnInfo(name = "host_image_url_element")
    var imageUrlElement: String?,

    @Ignore
    @SerializedName("patterns")
    var patterns: List<PatternSpecifiedData> = emptyList()
): IPatternHostData {

    constructor() : this(0, "", null, null, null, emptyList())

    constructor(
        id: Int,
        host: String,
        titleElement: String?,
        descriptionElement: String?,
        imageUrlElement: String?
    ) : this(id, host, titleElement, descriptionElement, imageUrlElement, emptyList())

    @Ignore override fun id(): Int = id
    @Ignore override fun host(): String = host
    @Ignore override fun hostTitleElement(): String? = titleElement
    @Ignore override fun hostDescriptionElement(): String? = descriptionElement
    @Ignore override fun hostImageUrlElement(): String? = imageUrlElement
}