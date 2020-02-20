package com.panat.mvvm.retrofit.viewModel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.panat.mvvm.retrofit.view.*

class MainViewModel(private val context: Context) : ViewModel() {

    private fun goGitEvent() {
        val intent = Intent(context, GitEventActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun goSocket() {
        val intent = Intent(context, SocketActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun goUpload() {
        val intent = Intent(context, UploadActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun goFCM() {
        val intent = Intent(context, FirebaseCloudMessagingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun goRWFolder() {
        val intent = Intent(context, ReadWriteFolderActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun go(position: Int) {
        when (position) {
            0 -> {
                goGitEvent()
            }
            1 -> {
                goSocket()
            }
            2 -> {
                goUpload()
            }
            3 -> {
                goFCM()
            }
            4 -> {
                goRWFolder()
            }
            else -> {

            }
        }
    }

}