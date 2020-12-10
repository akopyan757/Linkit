package com.akopyan757.linkit.model.entity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.akopyan757.urlparser.IUrlDataFactory
import com.akopyan757.urlparser.UrlData

@Entity(tableName = "url_link_data")
data class UrlLinkData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0L,
    @ColumnInfo(name = "url") var url: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "photo_url") var photoUrl: String? = null,
    @ColumnInfo(name = "logo_url") var logoUrl: String? = null,
    @ColumnInfo(name = "folder_id") var folderId: Int = 0,
    @ColumnInfo(name = "pattern_host") var hostPatternId: Int = 0,
    @ColumnInfo(name = "pattern_specified") var specPatternId: Int = 0,
    @ColumnInfo(name = "_order") val _order: Int = 0,
): UrlData(), ILink {

    @Ignore var logoBitmap: Bitmap? = null
    @Ignore var contentBitmap: Bitmap? = null

    override fun linkId() = id
    override fun linkGroupId() = folderId
    override fun linkOrder(): Int = _order

    override var dataUrl: String
        get() = url
        set(value) { url = value }

    override var dataTitle: String
        get() = title
        set(value) { title = value }

    override var dataDescription: String
        get() = description
        set(value) { description = value }

    override var dataImageContentUrl: String?
        get() = photoUrl
        set(value) { photoUrl = value }

    override var dataLogoContentUrl: String?
        get() = logoUrl
        set(value) { logoUrl = value }

    override var patternHostId: Int
        get() = hostPatternId
        set(value) { hostPatternId = value }

    override var patternSpecifiedId: Int
        get() = specPatternId
        set(value) { specPatternId = value }

    class Factory: IUrlDataFactory<UrlLinkData> {
        override fun createData() = UrlLinkData()
    }
}