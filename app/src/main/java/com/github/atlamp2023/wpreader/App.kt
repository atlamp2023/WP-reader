package com.github.atlamp2023.wpreader

import android.app.Application
import com.github.atlamp2023.wpreader.core.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            //androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                appModule,
                domainListModule,
                domainDetailModule,
                dataListModule,
                dataDetailModule
            )
        }
    }
}