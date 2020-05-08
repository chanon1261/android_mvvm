package com.panat.mvvm.retrofit.view

import android.os.Bundle
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.panat.mvvm.retrofit.databinding.ActivityReadWriteFolderBinding
import com.panat.mvvm.retrofit.extension.toastLong
import com.snatik.storage.Storage
import java.io.File


class ReadWriteFolderActivity : BaseActivity() {

    private lateinit var storage: Storage
    private var internalPath = ""
    private lateinit var binding: ActivityReadWriteFolderBinding
    val path = internalPath + File.separator + "rwActivity" + File.separator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadWriteFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Dexter.withActivity(this).withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    gotoReadWrite()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    toastLong("onPermissionDenied")
                }

            })

    }

    private fun gotoReadWrite() {
        initStorage()
        printDirect()

        println(tag + storage.externalStorageDirectory)

        //storage.createDirectory(path)
        binding.create.setOnClickListener { v ->
            if (binding.folderName.text.toString().isNotEmpty()) {
                //storage.createDirectory(path + binding.folderName.text.toString())
                binding.folderName.setText("")
            }
        }
    }

    private fun initStorage() {
        storage = Storage(this)
        internalPath = storage.internalFilesDirectory
        internalPath = internalPath + File.separator + "sticker"

        if (!File(internalPath).exists())
            storage.createDirectory(internalPath)
    }

    private fun printDirect() {
        val allFile = storage.getFiles(path)
        allFile.forEach {
            if (it.isDirectory) {
                println(tag + "directory " + it.name.toString())
            }
        }
    }

    companion object {
        val tag = "ReadWriteFolderActivity "
    }
}
