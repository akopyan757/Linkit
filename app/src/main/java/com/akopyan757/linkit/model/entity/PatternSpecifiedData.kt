package com.akopyan757.linkit.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.akopyan757.urlparser.data.IPatternSpecifiedData
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pattern_specified_data")
data class PatternSpecifiedData(

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @ColumnInfo(name = "spec_id")
    var id: Int,

    @SerializedName("host_id")
    @ColumnInfo(name = "spec_host_id")
    var hostId: Int = 0,

    @SerializedName("pattern")
    @ColumnInfo(name = "spec_pattern")
    var pattern: String = "",

    @SerializedName("title_element")
    @ColumnInfo(name = "spec_title_element")
    var titleElement: String? = null,

    @SerializedName("description_element")
    @ColumnInfo(name = "spec_description_element")
    var descriptionElement: String? = null,

    @SerializedName("image_url_element")
    @ColumnInfo(name = "spec_image_url_element")
    var imageUrlElement: String? = null,

): IPatternSpecifiedData {

    constructor() : this(0, 0, "", null, null, null)

    @Ignore override fun specId() = id
    @Ignore override fun specHostId() = hostId
    @Ignore override fun specPattern() = pattern
    @Ignore override fun specTitleElement() = titleElement
    @Ignore override fun specDescriptionElement() = descriptionElement
    @Ignore override fun specImageUrlElement() = imageUrlElement
}