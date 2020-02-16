package com.panat.mvvm.retrofit.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.panat.mvvm.retrofit.BuildConfig
import com.panat.mvvm.retrofit.service.ApiService
import com.panat.mvvm.retrofit.service.UploadService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val NetworkModule = module {
    factory { provideGithubService() }
    factory { provideUpload() }
}

fun getClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    return OkHttpClient.Builder().addInterceptor(interceptor).build()
}

fun provideGithubService(): ApiService {
    return Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(getClient())
        .build()
        .create(ApiService::class.java)
}

fun provideUpload(): UploadService {
    return Retrofit.Builder()
        .baseUrl("http://192.168.1.1:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(getClient())
        .build()
        .create(UploadService::class.java)
}
