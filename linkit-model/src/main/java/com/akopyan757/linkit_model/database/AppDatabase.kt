package com.akopyan757.linkit_model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.akopyan757.linkit_model.database.data.FolderData
import com.akopyan757.linkit_model.database.data.UrlLinkData

@Database(entities = [
    UrlLinkData::class,
    FolderData::class
], version = 23)
public abstract class AppDatabase: RoomDatabase() {
    public abstract fun urlLinkDao(): UrlLinkDao
    public abstract fun folderDao(): FolderDao

    companion object {
        private const val DATABASE_NAME = "DATABASE_NAME"

        fun createAppDatabaseInstance(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}