package com.akopyan757.linkit.common.di

import com.akopyan757.linkit.common.Config
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ContextModule {

    val it = module {
        single(named(Config.IO_DISPATCHERS)) {
            Dispatchers.IO
        }
    }
}