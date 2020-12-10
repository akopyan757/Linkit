package com.akopyan757.linkit

import android.app.Application
import com.akopyan757.linkit.common.di.CacheModule
import com.akopyan757.linkit.common.di.DatabaseModule
import com.akopyan757.linkit.common.di.ModelModule
import com.akopyan757.linkit.common.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            // declare modules
            modules(listOf(CacheModule.it, DatabaseModule.module, ModelModule.module,
                ViewModelModule.module))
        }


    }
}