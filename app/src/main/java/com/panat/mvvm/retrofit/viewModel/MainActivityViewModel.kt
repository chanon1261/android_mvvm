package com.panat.mvvm.retrofit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panat.mvvm.retrofit.model.GithubEvents
import com.panat.mvvm.retrofit.service.ApiService
import com.panat.mvvm.retrofit.service.GitRepository
import kotlinx.coroutines.*


class MainActivityViewModel(private val retrofit: ApiService) : ViewModel() {

    private val _events = MutableLiveData<List<GithubEvents>>()
    val events: LiveData<List<GithubEvents>>
        get() = _events

    val git = GitRepository()

    fun loadEvents() {
//        retrofit.getEvents().enqueue(object : Callback<List<GithubEvents>> {
//            override fun onResponse(
//                call: Call<List<GithubEvents>>,
//                response: Response<List<GithubEvents>>
//            ) {
//                _events.postValue(response.body())
//            }
//
//            override fun onFailure(call: Call<List<GithubEvents>>, t: Throwable) {
//                println("GithubEvents onFailure")
//            }
//        })
//        retrofit.getEvents().cancel()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = async { git.getEvent() }
                withContext(Dispatchers.Main) {
                    if (response.await().isSuccessful) {
                        _events.postValue(response.await().body())
                    }
                }
                println("GithubEvents $response")
            } catch (e: Exception) {
                println("GithubEvents CoroutineScope Exception ${e.message}")
            }
        }

    }
}