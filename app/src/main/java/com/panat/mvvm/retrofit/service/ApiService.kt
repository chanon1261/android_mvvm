package com.panat.mvvm.retrofit.service

import com.panat.mvvm.retrofit.model.GitEvent.GithEvents
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("events")
    fun getEvents(): Call<List<GithEvents>>
}
