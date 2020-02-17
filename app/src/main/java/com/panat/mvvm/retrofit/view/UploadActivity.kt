package com.panat.mvvm.retrofit.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.databinding.DataBindingUtil
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.panat.mvvm.retrofit.MainApp
import com.panat.mvvm.retrofit.R
import com.panat.mvvm.retrofit.databinding.ActivityUploadBinding
import com.panat.mvvm.retrofit.di.getClient
import com.panat.mvvm.retrofit.di.provideUpload
import com.panat.mvvm.retrofit.service.UploadService
import com.panat.mvvm.retrofit.utils.FileHelper
import com.panat.mvvm.retrofit.viewModel.UpLoadViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody.Part.createFormData
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.jetbrains.anko.toast
import org.json.JSONObject
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class UploadActivity : BaseActivity() {


    private lateinit var binding: ActivityUploadBinding
    val viewModel: UpLoadViewModel by inject()
    private lateinit var retrofit: UploadService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrofit = provideUpload()
        setupView()

        binding.btnUploadImg.setOnClickListener {
            checkPermission()
        }

        retrofit.test().enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                println("ok ${response.body()?.string()}")
            }

        })

    }

    override fun setupView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload)
        title = "Upload"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.lifecycleOwner = this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == viewModel.GALLERY_REQUEST_CODE) {
            binding.image.setImageURI(data?.data)
            data?.let { upload(it) }
        }
    }


    private fun upload(data: Intent) {

        val uri = data.data!!

        val fileHelper = FileHelper()
        val realPath = fileHelper.getPathFromURI(this, uri)
        val file = fileHelper.createFile(realPath!!)

//        uploadFromAndroidFastNetwork(file)
        uploadFromRetrofit(uri)

    }

    private fun uploadFromRetrofit(uri: Uri) {

        val fileHelper = FileHelper()
        val realPath = fileHelper.getPathFromURI(this, uri)
        val file = fileHelper.createFile(realPath!!)

        val mediaType: MediaType = MediaType.parse(getMimeType(realPath))!!
        val requestBody =  RequestBody.create(mediaType, file)
        val filePart = createFormData("file", file.name, requestBody)

        val call = retrofit.upload(filePart)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("OkHttp onFailure ${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                println("OkHttp onResponse")
            }
        })
    }

    private fun uploadFromAndroidFastNetwork(file: File) {

        AndroidNetworking.upload(MainApp.BaseUrl + "dev/upload/photo/33")
            .addMultipartFile("file", file)
            .setTag(this)
            .setOkHttpClient(null)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    println("onResponse: $response")
                }

                override fun onError(anError: ANError) {
                    println("onError: $anError")
                    anError.printStackTrace()
                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun checkPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report?.areAllPermissionsGranted()!!) {
                        pickFromGallery()
                    }
                    toast("permission denied")
                }
            })
            .check()
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, viewModel.GALLERY_REQUEST_CODE)
    }

    fun getMimeType(path: String): String {
        var type = "image/jpeg" // Default Value
        val extension = MimeTypeMap.getFileExtensionFromUrl(path);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension).toString()
        }
        return type
    }
}
