package com.panat.mvvm.retrofit.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panat.mvvm.retrofit.di.provideGithubService
import com.panat.mvvm.retrofit.model.GitEvent.GithEvents
import com.panat.mvvm.retrofit.repository.GitRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GitEventViewModel(private val repository: GitRepository) : ViewModel() {
    val events: LiveData<List<GithEvents>>
        get() = repository.get()
}