package com.panat.mvvm.retrofit

import android.app.Application
import com.panat.mvvm.retrofit.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApp)
            modules(appModule)
        }
    }

    companion object {
        const val BaseUrl = "http://10.168.55.101:8080/"
    }
}
