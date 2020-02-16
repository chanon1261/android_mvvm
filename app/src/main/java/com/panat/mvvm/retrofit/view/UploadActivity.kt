package com.panat.mvvm.retrofit.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.panat.mvvm.retrofit.R
import com.panat.mvvm.retrofit.databinding.ActivityUploadBinding
import com.panat.mvvm.retrofit.di.provideUpload
import com.panat.mvvm.retrofit.viewModel.UpLoadViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Response
import java.io.File


class UploadActivity : BaseActivity() {


    private lateinit var binding: ActivityUploadBinding
    val viewModel: UpLoadViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnUploadImg.setOnClickListener {
            checkPermission()
        }
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

        val uri = data.data
        val retrofit = provideUpload()

        val filePath = uri?.path!!
        val file = File(filePath)
        println("OkHttp  Filename " + file.name)
        val mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)

        retrofit.upload(fileToUpload).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                toast("onFailure")
                println("OkHttp : onFailure")
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                toast("onResponse")
                println("OkHttp : $response")
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
}
