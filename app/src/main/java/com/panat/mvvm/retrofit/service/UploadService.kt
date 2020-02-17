package com.panat.mvvm.retrofit.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface UploadService {

    @Multipart
    @POST("dev/upload/photo/33")
    fun upload(
        @Part file: MultipartBody.Part
    ): Call<ResponseBody>

    @Multipart
    @POST("dev/upload/photo/33")
    fun upload2(
        @Part("file") file: RequestBody
    ): Call<ResponseBody>

    @POST("/dev/test")
    fun test(): Call<ResponseBody>

}