package com.panat.mvvm.retrofit.view

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.panat.mvvm.retrofit.R
import com.panat.mvvm.retrofit.databinding.ActivityUploadBinding
import com.panat.mvvm.retrofit.di.provideUpload
import com.panat.mvvm.retrofit.service.UploadService
import com.panat.mvvm.retrofit.viewModel.UpLoadViewModel
import org.jetbrains.anko.progressDialog
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject


class UploadActivity : BaseActivity() {

    private lateinit var binding: ActivityUploadBinding
    val viewModel: UpLoadViewModel by inject()
    private lateinit var retrofit: UploadService
    val GALLERY_REQUEST_CODE = 101
    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrofit = provideUpload()
        setupView()

        binding.btnUploadImg.setOnClickListener {
            checkPermission()
        }

        viewModel.success.observe(this, androidx.lifecycle.Observer {
            binding.image.setImageURI(uri)
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
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            //binding.image.setImageURI(data?.data)
            data?.let {
                upload(it)
                it.data?.run {
                    uri = this
                }
            }
        }
    }


    private fun upload(data: Intent) {
        val uri = data.data!!
        viewModel.uploadPicture(uri)
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
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

}
