package com.akopyan757.linkit

import android.app.Application
import android.util.Log
import com.akopyan757.linkit.common.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            // declare modules
            modules(
                listOf(
                    ContextModule.it, DatabaseModule.module, ModelModule.module,
                    ViewModelModule.module, ServiceModule.module, NetworkModule.module
                )
            )
        }


        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("APP", "DefaultException handler: $thread", throwable)
        }

    }
}