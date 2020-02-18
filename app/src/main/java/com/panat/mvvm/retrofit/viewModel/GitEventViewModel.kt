package com.panat.mvvm.retrofit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panat.mvvm.retrofit.model.GitEvent.GithubEvents
import com.panat.mvvm.retrofit.repository.GitRepository
import kotlinx.coroutines.*


class GitEventViewModel : ViewModel() {

    private val _events = MutableLiveData<List<GithubEvents>>()
    val events: LiveData<List<GithubEvents>>
        get() = _events

    private val gitRepository = GitRepository()

    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = async { gitRepository.getEvent() }
                withContext(Dispatchers.Main) {
                    if (response.await().isSuccessful) {
                        _events.postValue(response.await().body())
                    }
                }
                //println("GithubEvents ${response.await().body()}")
            } catch (e: Exception) {
                println("GithubEvents CoroutineScope Exception ${e.message}")
            }
        }
    }
}