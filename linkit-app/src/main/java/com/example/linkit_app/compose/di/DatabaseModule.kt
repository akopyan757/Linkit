package com.example.linkit_app.compose.di

import com.akopyan757.linkit_model.database.AppDatabase
import com.example.linkit_app.compose.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

object DatabaseModule {

    val module = module {
        single { FirebaseFirestore.getInstance() }
        single { FirebaseAuth.getInstance() }

        single(named(Config.LINKS)) { get<FirebaseFirestore>().collection(Config.USERS) }

        single { AppDatabase.createAppDatabaseInstance(androidContext()) }
        single { get<AppDatabase>().urlLinkDao() }
        single { get<AppDatabase>().folderDao() }
    }
}