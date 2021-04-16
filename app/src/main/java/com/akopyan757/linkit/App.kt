package com.akopyan757.linkit

import android.app.Application
import android.util.Log
import com.akopyan757.linkit.common.di.DatabaseModule
import com.akopyan757.linkit.common.di.ModelModule
import com.akopyan757.linkit.common.di.UseCaseModule
import com.akopyan757.linkit.common.di.ViewModelModule
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
                    UseCaseModule.module, ServiceModule.module,
                )
            )

        }

        BannerViewExtension.init(this)

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("APP", "DefaultException handler: $thread", throwable)
        }

    }
}