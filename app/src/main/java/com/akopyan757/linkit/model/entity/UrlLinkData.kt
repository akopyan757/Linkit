package com.akopyan757.linkit.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude

@Entity(tableName = "url_link_data")
data class UrlLinkData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Long = 0L,
    @ColumnInfo(name = "url") var url: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "photo_url") var photoUrl: String? = null,
    @ColumnInfo(name = "logo_url") var logoUrl: String? = null,
    @ColumnInfo(name = "folder_id") var folderId: Int = 0,
    @ColumnInfo(name = "_order") var _order: Int = 0,
) {

    @Exclude @Ignore var logoFileName: String? = null
    @Exclude @Ignore var contentFileName: String? = null
    @Exclude @Ignore var screenshotFileName: String? = null

    companion object {
        fun createWithAssignFolder(url: String, folderId: Int) = UrlLinkData().apply {
            this.url = url
            this.folderId = folderId
        }
    }
}