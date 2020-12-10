package com.akopyan757.linkit.common.di

import com.akopyan757.linkit.model.cache.ImageCache
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object CacheModule {

    val it = module {
        single { androidContext().cacheDir }
        single { ImageCache() }
    }
}