package com.akopyan757.linkit.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "url_link_data")
data class UrlLinkData(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "url") var url: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "photo_url") var photoUrl: String? = null,
    @ColumnInfo(name = "logo_url") var logoUrl: String? = null,
    @ColumnInfo(name = "folder_id") var folderId: String? = null,
    @ColumnInfo(name = "_order") var _order: Int = 0,
)