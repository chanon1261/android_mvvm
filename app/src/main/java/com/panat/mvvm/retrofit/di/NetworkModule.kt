package com.panat.mvvm.retrofit.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.panat.mvvm.retrofit.BuildConfig
import com.panat.mvvm.retrofit.MainApp
import com.panat.mvvm.retrofit.service.ApiService
import com.panat.mvvm.retrofit.service.UploadService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val NetworkModule = module {
    factory { provideGithubService() }
    factory { provideUpload() }
    single { getClient() }
}

fun getClient(): OkHttpClient {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

    val httpClient = OkHttpClient.Builder()

    httpClient.connectTimeout(100, TimeUnit.SECONDS)
    httpClient.readTimeout(100, TimeUnit.SECONDS)
    httpClient.writeTimeout(100, TimeUnit.SECONDS)
    httpClient.addInterceptor(interceptor)
    httpClient.retryOnConnectionFailure(true)

    return httpClient.build()
//    return OkHttpClient.Builder().addInterceptor(interceptor).build()
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
        .baseUrl(MainApp.BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(UploadService::class.java)
}
