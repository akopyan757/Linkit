package com.example.linkit_app

import android.app.Application
import android.util.Log
import com.example.linkit_app.compose.di.DatabaseModule
import com.example.linkit_app.compose.di.ModelModule
import com.example.linkit_app.compose.di.UseCaseModule
import com.example.linkit_app.compose.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    DatabaseModule.module, ModelModule.module, ViewModelModule.module,
                    UseCaseModule.module
                )
            )

        }

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("APP", "DefaultException handler: $thread", throwable)
        }
    }
}