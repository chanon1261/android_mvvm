package com.panat.mvvm.retrofit.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.database.Cursor
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.panat.mvvm.retrofit.databinding.ActivityUploadBinding
import com.panat.mvvm.retrofit.di.provideUpload
import com.panat.mvvm.retrofit.service.UploadService
import com.panat.mvvm.retrofit.viewModel.UpLoadViewModel
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import java.io.File


class UploadActivity() : BaseActivity() {

    private lateinit var binding: ActivityUploadBinding
    val viewModel: UpLoadViewModel by inject()
    private lateinit var retrofit: UploadService
    val GALLERY_REQUEST_CODE = 101

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrofit = provideUpload()
        setupView()

        binding.btnUploadImg.setOnClickListener {
            checkPermission()
        }

        viewModel.success.observe(this, Observer {
            binding.determinateBar.visibility = View.INVISIBLE
            binding.percent.visibility = View.INVISIBLE
            binding.image.alpha = 1F
        })

        viewModel.process.observe(this, Observer {
            binding.determinateBar.progress = it.toInt()
            binding.percent.text = "$it%"
        })
    }

    override fun setupView() {
        binding =
            DataBindingUtil.setContentView(this, com.panat.mvvm.retrofit.R.layout.activity_upload)
        title = "Upload"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.lifecycleOwner = this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            binding.image.alpha = 0.5F

            val uri = Matisse.obtainResult(data).first()
            val path = uri.path

            path?.let {
                if (it.contains("/video/")) {
                    Log.d(this.javaClass.name, "Video")
                    beforeUploadVideo(uri)
                }
                if (it.contains("/images/")) {
                    Log.d(this.javaClass.name, "Image")
                    beforeUploadImage(uri)
                }
            }
        }
    }

    private fun beforeUploadImage(data: Uri) {
        binding.image.setImageURI(data)
        binding.determinateBar.visibility = View.VISIBLE
        binding.percent.visibility = View.VISIBLE
        viewModel.uploadPicture(data)
    }

    private fun beforeUploadVideo(uri: Uri) {

        val path = getRealPathFromURI(this, uri)
        val file = File(path)


        binding.determinateBar.visibility = View.VISIBLE
        binding.percent.visibility = View.VISIBLE

        val signal = CancellationSignal()
//        signal.setOnCancelListener()

        val b1Map = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND)
        //val bMap = ThumbnailUtils.createVideoThumbnail(file, Size(120, 120), signal)
        binding.image.setImageBitmap(b1Map)
        viewModel.uploadVideo(path)

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
                    toast("permission denied")
                    token?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report?.areAllPermissionsGranted()!!) {
                        pickFromGallery()
                    }
                }
            })
            .check()
    }

    private fun pickFromGallery() {
        Matisse.from(this)
            .choose(MimeType.ofAll())
            .showSingleMediaType(true)
            .countable(true)
            .maxSelectable(1)
            .gridExpectedSize(400)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.45f)
            .imageEngine(GlideEngine())
            .forResult(GALLERY_REQUEST_CODE)
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Video.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

}
