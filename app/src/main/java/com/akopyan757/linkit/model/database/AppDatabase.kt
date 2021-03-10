package com.akopyan757.linkit.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.entity.UrlLinkData

@Database(entities = [
    UrlLinkData::class,
    FolderData::class
], version = 18)
abstract class AppDatabase: RoomDatabase() {
    abstract fun urlLinkDao(): UrlLinkDao
    abstract fun folderDao(): FolderDao
}