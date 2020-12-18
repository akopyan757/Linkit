package com.akopyan757.linkit.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.entity.PatternHostData
import com.akopyan757.linkit.model.entity.PatternSpecifiedData
import com.akopyan757.linkit.model.entity.UrlLinkData

@Database(entities = [
    UrlLinkData::class,
    FolderData::class,
    PatternHostData::class,
    PatternSpecifiedData::class
], version = 16)
abstract class AppDatabase: RoomDatabase() {
    abstract fun urlLinkDao(): UrlLinkDao
    abstract fun folderDao(): FolderDao
    abstract fun patternDao(): PatternDao
}