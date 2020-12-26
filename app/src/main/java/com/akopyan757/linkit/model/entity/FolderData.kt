package com.akopyan757.linkit.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folder_data")
class FolderData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "order") val order: Int = 0,
) {
    companion object {
        const val GENERAL_FOLDER_ID = 1
        private const val GENERAL_FOLDER_NAME = "All"
        private const val GENERAL_FOLDER_ORDER = 0

        fun generalFolder() = FolderData(
            GENERAL_FOLDER_ID,
            GENERAL_FOLDER_NAME,
            GENERAL_FOLDER_ORDER
        )
    }
}