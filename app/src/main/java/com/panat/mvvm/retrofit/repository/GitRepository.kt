package com.panat.mvvm.retrofit.repository

import androidx.lifecycle.MutableLiveData
import com.panat.mvvm.retrofit.di.provideGithubService
import com.panat.mvvm.retrofit.model.GitEvent.GithubEvents
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GitRepository {
    private var client = provideGithubService()
    suspend fun getEvent() = client.getEventsCoroutines()

    fun get(): MutableLiveData<List<GithubEvents>> {
        val newsData = MutableLiveData<List<GithubEvents>>()
        client.getEvents().enqueue(object : Callback<List<GithubEvents>> {
            override fun onFailure(call: Call<List<GithubEvents>>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<List<GithubEvents>>,
                response: Response<List<GithubEvents>>
            ) {
                newsData.postValue(response.body())
            }

        })
        return newsData
    }
}