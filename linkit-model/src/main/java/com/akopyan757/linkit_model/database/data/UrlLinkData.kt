package com.akopyan757.linkit_model.database.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
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
    @ColumnInfo(name = "folder_id") var folderId: String? = null,
    @ColumnInfo(name = "site") var site: String? = null,
    @ColumnInfo(name = "type") var type: String? = null,
    @ColumnInfo(name = "_order") var _order: Int = 0,
    @ColumnInfo(name = "collapsed") var collapsed: Boolean = false,
    @Embedded var app: UrlLinkAppData? = null
)