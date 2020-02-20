package com.panat.mvvm.retrofit.repository

import com.panat.mvvm.retrofit.di.provideGithubService

class GitRepository {
    private var client = provideGithubService()
    suspend fun getEvent() = client.getEventsCoroutines()
}