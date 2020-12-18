package com.akopyan757.linkit.common.di

import com.akopyan757.linkit.common.Config
import com.akopyan757.linkit.common.utils.JsonPatternsParser
import com.akopyan757.linkit.model.cache.ImageCache
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ContextModule {

    val it = module {
        single { androidContext().cacheDir }
        single { JsonPatternsParser(androidContext().assets) }
        single(named(Config.IO_DISPATCHERS)) { Dispatchers.IO }
    }
}