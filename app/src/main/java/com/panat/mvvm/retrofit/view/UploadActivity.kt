package com.panat.mvvm.retrofit.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.panat.mvvm.retrofit.R
import com.panat.mvvm.retrofit.databinding.ActivityUploadBinding
import com.panat.mvvm.retrofit.di.provideUpload
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Response
import java.io.File


class UploadActivity : AppCompatActivity() {

    private val GALLERY_REQUEST_CODE = 101
    private lateinit var binding: ActivityUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload)
        title = "Upload"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.lifecycleOwner = this

        binding.btnUploadImg.setOnClickListener {
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            binding.image.setImageURI(data?.data)
            data?.let { upload(it) }
        }
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun upload(data: Intent) {

        val uri = data.data
        val retrofit = provideUpload()


        val filePath = uri?.path!!
        val file = File(filePath)
        Log.d("OkHttp", "Filename " + file.getName())
        //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
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
}
