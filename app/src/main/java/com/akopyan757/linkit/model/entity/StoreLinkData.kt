package com.akopyan757.linkit.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "store_link_data")
data class StoreLinkData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0L,
    @ColumnInfo(name = "uri") val uri: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "size") val size: Long,
    @ColumnInfo(name = "path") val path: String?,
    @ColumnInfo(name = "folder_ids") val folderIds: List<Int> = emptyList(),
    @ColumnInfo(name = "order") val order: Int = 0,
): ILink {
    override fun linkId() = id
    override fun linkGroupIds() = folderIds
    override fun linkOrder() = order
}