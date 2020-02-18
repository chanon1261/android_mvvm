package com.panat.mvvm.retrofit.viewModel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.panat.mvvm.retrofit.view.FirebaseCloudMessagingActivity
import com.panat.mvvm.retrofit.view.GitEventActivity
import com.panat.mvvm.retrofit.view.SocketActivity
import com.panat.mvvm.retrofit.view.UploadActivity

class MainViewModel(private val context: Context) : ViewModel() {

    val i = 0
    fun goGitEvent() {
        val intent = Intent(context, GitEventActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun goSocket() {
        val intent = Intent(context, SocketActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun goUpload() {
        val intent = Intent(context, UploadActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun goFCM() {
        val intent = Intent(context, FirebaseCloudMessagingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}