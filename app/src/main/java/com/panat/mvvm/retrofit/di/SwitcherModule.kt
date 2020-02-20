package com.panat.mvvm.retrofit.di

import android.content.Context
import android.view.View
import org.koin.dsl.module
import pl.aprilapps.switcher.Switcher


val SwitcherModule = module {
    single {
        provideSwitcher(get(), get(), get(), get(), get())
    }
}

fun provideSwitcher(content: View, empty: View, error: View, progress: View, context: Context): Switcher {
    return Switcher.Builder(context)
        .addContentView(content)
        .addErrorView(error)
        .addProgressView(progress)
//            .setErrorLabel( )
//            .setProgressLabel()
        .addEmptyView(empty)
        .setAnimDuration(400)
        .setLogsEnabled(true)
        .build()
}