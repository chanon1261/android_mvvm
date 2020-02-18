package com.panat.mvvm.retrofit.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.utils.Utils.getMimeType
import com.panat.mvvm.retrofit.service.UploadService
import com.panat.mvvm.retrofit.utils.FileHelper
import com.panat.mvvm.retrofit.utils.ProgressRequestBody
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpLoadViewModel(val retrofit: UploadService, val context: Context) : ViewModel() {


    private val _success = MutableLiveData<ResponseBody>()
    val success: LiveData<ResponseBody>
        get() = _success

    fun uploadPicture(uri: Uri) {
        val fileHelper = FileHelper()
        val realPath = fileHelper.getPathFromURI(context, uri)
        val file = fileHelper.createFile(realPath!!)

        val mediaType: MediaType = MediaType.parse(getMimeType(realPath))!!
        val requestBody = RequestBody.create(mediaType, file)


        val fileProgress = ProgressRequestBody(file, mediaType)


        val filePart = MultipartBody.Part.createFormData("file", file.name, fileProgress)


        fileProgress.getProgressSubject()
            .subscribeOn(Schedulers.io())
            .subscribe { percentage ->
                Log.i("PROGRESS", "$percentage%")
            }

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