package com.panat.mvvm.retrofit.Repository

import com.panat.mvvm.retrofit.di.provideGithubService

class GitRepository {
    var client = provideGithubService()
    suspend fun getEvent() = client.getEventsCoroutines()
}