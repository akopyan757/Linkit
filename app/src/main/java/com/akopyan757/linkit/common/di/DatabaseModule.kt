package com.akopyan757.linkit.common.di

import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.Config.LINKS
import com.akopyan757.linkit.common.Config.PATTERNS
import com.akopyan757.linkit.common.Config.USERS
import com.akopyan757.linkit_model.database.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

object DatabaseModule {

    val module = module {
        single(named(Config.CACHE_DIR)) { androidContext().cacheDir }
        single(named(Config.CACHE_IMAGE_DIR)) {
            File(androidContext().cacheDir , Config.CACHE_IMAGES_FOLDER)
        }

        single { FirebaseFirestore.getInstance() }
        single { FirebaseAuth.getInstance() }

        single(named(LINKS)) { get<FirebaseFirestore>().collection(USERS) }

        single { AppDatabase.createAppDatabaseInstance(androidContext()) }
        single { get<AppDatabase>().urlLinkDao() }
        single { get<AppDatabase>().folderDao() }
    }
}