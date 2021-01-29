package com.akopyan757.linkit.common.di

import com.akopyan757.linkit.model.firebase.AuthWrapper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object NetworkModule {

    val module = module {
        single { AuthWrapper(androidContext()) }
    }
}