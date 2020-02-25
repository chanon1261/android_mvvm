package com.panat.mvvm.retrofit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panat.mvvm.retrofit.di.provideGithubService
import com.panat.mvvm.retrofit.model.GitEvent.GithubEvents
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GitEventViewModel : ViewModel() {

    private val _events = MutableLiveData<List<GithubEvents>>()
    val events: LiveData<List<GithubEvents>>
        get() = _events

    fun start() {
        provideGithubService().getEvents().enqueue(object : Callback<List<GithubEvents>> {
            override fun onFailure(call: Call<List<GithubEvents>>, t: Throwable) {
            }
            override fun onResponse(
                call: Call<List<GithubEvents>>,
                response: Response<List<GithubEvents>>
            ) {
                _events.postValue(response.body())
            }

        })
    }
}