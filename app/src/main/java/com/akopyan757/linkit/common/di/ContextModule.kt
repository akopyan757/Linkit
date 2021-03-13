package com.akopyan757.linkit.common.di

import android.content.Context
import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.model.cache.TokenCache
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ContextModule {

    private const val KEY_SHARED = "SHARED_PREFS"

    val it = module {
        single(named(Config.IO_DISPATCHERS)) { Dispatchers.IO }
        single { androidContext().getSharedPreferences(KEY_SHARED, Context.MODE_PRIVATE) }
        single { TokenCache() }
    }
}