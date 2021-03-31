package com.akopyan757.linkit.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folder_data")
class FolderData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "order") var order: Int = 0
) {
    constructor() : this(0, "", 0)

    fun isGeneral() = id == GENERAL_FOLDER_ID

    companion object {
        const val GENERAL_FOLDER_ID = 1
        const val GENERAL_FOLDER_NAME_TITLE = "All folders"
        private const val GENERAL_FOLDER_NAME = "All"
        private const val GENERAL_FOLDER_ORDER = 0

        fun generalFolder() = FolderData(
            GENERAL_FOLDER_ID,
            GENERAL_FOLDER_NAME,
            GENERAL_FOLDER_ORDER
        )
    }
}