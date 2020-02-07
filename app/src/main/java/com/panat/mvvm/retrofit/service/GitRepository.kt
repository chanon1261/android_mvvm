package com.panat.mvvm.retrofit.service

import com.panat.mvvm.retrofit.di.provideRetrofit

class GitRepository {
    var client = provideRetrofit()
    suspend fun getEvent() = client.getEventsCoroutines()
}