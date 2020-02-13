package com.panat.mvvm.retrofit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panat.mvvm.retrofit.model.GithubEvents
import com.panat.mvvm.retrofit.service.ApiService
import com.panat.mvvm.retrofit.service.GitRepository
import kotlinx.coroutines.*


class GitEventActivityViewModel(private val retrofit: ApiService) : ViewModel() {

    private val _events = MutableLiveData<List<GithubEvents>>()
    val events: LiveData<List<GithubEvents>>
        get() = _events

    private val _events2 = MutableLiveData<List<GithubEvents>>()
    val events2: LiveData<List<GithubEvents>>
        get() = _events2

    val gitRepository = GitRepository()

    fun loadEvents() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = async { gitRepository.getEvent() }
                val response2 = async { gitRepository.getEvent() }
                withContext(Dispatchers.Main) {
                    if (response.await().isSuccessful) {
                        _events.postValue(response.await().body())
                    }
                    if (response2.await().isSuccessful) {
                        _events2.postValue(response.await().body())
                    }
                }
                println("GithubEvents $response")
            } catch (e: Exception) {
                println("GithubEvents CoroutineScope Exception ${e.message}")
            }
        }
    }
}