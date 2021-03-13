package com.akopyan757.linkit.common.di

import android.util.Log
import androidx.room.Room
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.Config.LINKS
import com.akopyan757.linkit.common.Config.PATTERNS
import com.akopyan757.linkit.common.Config.USERS
import com.akopyan757.linkit.model.cache.ImageCache
import com.akopyan757.linkit.model.database.AppDatabase
import com.akopyan757.linkit.model.store.StoreLinks
import com.akopyan757.linkit.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.core.scope.ScopeCallback
import org.koin.dsl.module
import java.io.File

object DatabaseModule {

    val module = module {

        scope(named<MainActivity>()) {
            scoped(named(Config.CACHE_DIR)) { androidContext().cacheDir }
            scoped(named(Config.CACHE_IMAGE_DIR)) {
                File(androidContext().cacheDir , Config.CACHE_IMAGES_FOLDER)
            }

            scoped { ImageCache() }

            scoped { FirebaseDatabase.getInstance() }
            scoped { FirebaseFirestore.getInstance() }
            scoped { FirebaseAuth.getInstance() }

            scoped(named(PATTERNS)) { get<FirebaseDatabase>().getReference(PATTERNS) }
            scoped(named(LINKS)) { get<FirebaseFirestore>().collection(USERS) }

            scoped { StoreLinks() }

            scoped {
                val userId = getProperty(Config.KEY_USER_ID, Config.EMPTY)
                val databaseName = Config.DATABASE_NAME + "_" + userId
                Room.databaseBuilder(androidContext(), AppDatabase::class.java, databaseName)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { database ->
                        registerCallback(object : ScopeCallback {
                            override fun onScopeClose(scope: Scope) {
                                database.close()
                            }
                        })
                    }
            }

            scoped { get<AppDatabase>().urlLinkDao() }
            scoped { get<AppDatabase>().folderDao() }
        }
    }
}