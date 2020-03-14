package com.panat.mvvm.retrofit.repository

import androidx.lifecycle.MutableLiveData
import com.panat.mvvm.retrofit.model.GitEvent.GithEvents
import com.panat.mvvm.retrofit.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GitRepository(val retrofit: ApiService) {
    fun get(): MutableLiveData<List<GithEvents>> {
        val data = MutableLiveData<List<GithEvents>>()
        retrofit.getEvents().enqueue(object : Callback<List<GithEvents>> {
            override fun onFailure(call: Call<List<GithEvents>>, t: Throwable) {
                data.postValue(null)
            }
            override fun onResponse(call: Call<List<GithEvents>>, response: Response<List<GithEvents>>) {
                data.postValue(response.body())
            }
        })
        return data
    }
}