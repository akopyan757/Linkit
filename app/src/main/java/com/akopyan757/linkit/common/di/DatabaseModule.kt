package com.akopyan757.linkit.common.di

import com.akopyan757.linkit.common.Config.LINKS
import com.akopyan757.linkit.common.Config.USERS
import com.akopyan757.linkit_model.database.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

object DatabaseModule {

    val module = module {
        single { FirebaseFirestore.getInstance() }
        single { FirebaseAuth.getInstance() }

        single(named(LINKS)) { get<FirebaseFirestore>().collection(USERS) }

        single { AppDatabase.createAppDatabaseInstance(androidContext()) }
        single { get<AppDatabase>().urlLinkDao() }
        single { get<AppDatabase>().folderDao() }
    }
}