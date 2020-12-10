package com.akopyan757.linkit.common.di

import androidx.room.Room
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object DatabaseModule {


    val module = module {

        single {
            Room.databaseBuilder(androidContext(), AppDatabase::class.java, Config.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }

        single { get<AppDatabase>().urlLinkDao() }
        single { get<AppDatabase>().folderDao() }
        single { get<AppDatabase>().patternDao() }
    }
}