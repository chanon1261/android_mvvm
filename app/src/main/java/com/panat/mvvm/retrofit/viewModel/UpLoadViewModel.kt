package com.panat.mvvm.retrofit.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.utils.Utils.getMimeType
import com.panat.mvvm.retrofit.service.UploadService
import com.panat.mvvm.retrofit.utils.ProgressRequestBody
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UpLoadViewModel(val retrofit: UploadService, val context: Context) : ViewModel() {

    private val _success = MutableLiveData<ResponseBody>()
    val success: LiveData<ResponseBody>
        get() = _success

    private val _process = MutableLiveData<Float>()
    val process: LiveData<Float>
        get() = _process


    fun upload(path: String) {
        val file = File(path)
        val mediaType: MediaType = MediaType.parse(getMimeType(path))!!
        val fileProgress = ProgressRequestBody(file, mediaType)
        val filePart = MultipartBody.Part.createFormData("file", file.name, fileProgress)
        fileProgress.getProgressSubject()
            .subscribeOn(Schedulers.io())
            .subscribe { percentage ->
                _process.postValue(percentage)
            }
        upload(filePart)
    }

    private fun upload(filePart: MultipartBody.Part) {
        val call = retrofit.upload(filePart)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                context.toast("upload ${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                context.toast("upload success")
                _success.postValue(response.body())
            }
        })
    }


}