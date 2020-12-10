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
    val id: Int,

    @SerializedName("host_id")
    @ColumnInfo(name = "spec_host_id")
    var hostId: Int = 0,

    @SerializedName("pattern")
    @ColumnInfo(name = "spec_pattern")
    val pattern: String = "",

    @SerializedName("title_element")
    @ColumnInfo(name = "spec_title_element")
    val titleElement: String? = null,

    @SerializedName("description_element")
    @ColumnInfo(name = "spec_description_element")
    val descriptionElement: String? = null,

    @SerializedName("image_url_element")
    @ColumnInfo(name = "spec_image_url_element")
    val imageUrlElement: String? = null,

): IPatternSpecifiedData {

    @Ignore override fun specId() = id
    @Ignore override fun specHostId() = hostId
    @Ignore override fun specPattern() = pattern
    @Ignore override fun specTitleElement() = titleElement
    @Ignore override fun specDescriptionElement() = descriptionElement
    @Ignore override fun specImageUrlElement() = imageUrlElement
}