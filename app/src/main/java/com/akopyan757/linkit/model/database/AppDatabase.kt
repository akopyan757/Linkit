package com.akopyan757.linkit.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.akopyan757.linkit.model.entity.FolderData
import com.akopyan757.linkit.model.entity.PatternData
import com.akopyan757.linkit.model.entity.StoreLinkData
import com.akopyan757.linkit.model.entity.UrlLinkData

@Database(entities = [
    UrlLinkData::class,
    StoreLinkData::class,
    FolderData::class,
    PatternData::class
], version = 11)
@TypeConverters(value = [Converters::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun urlLinkDao(): UrlLinkDao
    abstract fun storeLinkDao(): StoreLinkDao
    abstract fun folderDao(): FolderDao
    abstract fun patternDao(): PatternDao
}