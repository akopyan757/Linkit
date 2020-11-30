package com.akopyan757.linkit.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.akopyan757.urlparser.IPatternData
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pattern_data")
data class PatternData(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "is_base")
    @SerializedName("is_base")
    val base: Boolean,
    @ColumnInfo(name = "pattern")
    @SerializedName("pattern")
    val pattern: String,
    @ColumnInfo(name = "title_element")
    @SerializedName("title_element")
    val titleElement: String?,
    @ColumnInfo(name = "description_element")
    @SerializedName("description_element")
    val descriptionElement: String?,
    @ColumnInfo(name = "image_url_element")
    @SerializedName("image_url_element")
    val imageUrlElement: String?
): IPatternData {
    @Ignore override fun pattern() = pattern
    @Ignore override fun isBase() = base
    @Ignore override fun titleElement() = titleElement
    @Ignore override fun descriptionElement() = descriptionElement
    @Ignore override fun imageUrlElement() = imageUrlElement
}