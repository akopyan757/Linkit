package com.cheesecake.linkit

import android.app.Application
import android.util.Log
import com.cheesecake.linkit.compose.di.DatabaseModule
import com.cheesecake.linkit.compose.di.ModelModule
import com.cheesecake.linkit.compose.di.UseCaseModule
import com.cheesecake.linkit.compose.di.ViewModelModule
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