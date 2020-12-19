package com.akopyan757.linkit.common.di

import androidx.room.Room
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.Config.PATTERNS
import com.akopyan757.linkit.model.cache.ImageCache
import com.akopyan757.linkit.model.database.AppDatabase
import com.akopyan757.linkit.model.store.StorePatterns
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

object DatabaseModule {

    val module = module {

        single { ImageCache() }

        single { FirebaseDatabase.getInstance() }
        single(named(PATTERNS)) { get<FirebaseDatabase>().getReference(PATTERNS) }
        single { StorePatterns() }

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