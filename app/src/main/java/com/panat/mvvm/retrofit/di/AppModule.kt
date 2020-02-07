package com.panat.mvvm.retrofit.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.panat.mvvm.retrofit.BuildConfig
import com.panat.mvvm.retrofit.service.ApiService
import com.panat.mvvm.retrofit.viewModel.MainActivityViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    viewModel { MainActivityViewModel( get() ) }
    factory { provideRetrofit() }
}

fun provideRetrofit(): ApiService {

    val interceptor = HttpLoggingInterceptor();
    interceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    return Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(client)
        .build()
        .create(ApiService::class.java)
}
