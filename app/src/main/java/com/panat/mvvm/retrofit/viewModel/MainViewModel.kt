package com.panat.mvvm.retrofit.viewModel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.panat.mvvm.retrofit.view.GitEventActivity
import com.panat.mvvm.retrofit.view.SocketActivity

class MainViewModel(private val context: Context) : ViewModel() {

    val i = 0
    fun goGitEvent(){
        val intent = Intent(context,GitEventActivity::class.java)
        context.startActivity(intent)
    }

    fun goSocket(){
        val intent = Intent(context,SocketActivity::class.java)
        context.startActivity(intent)
    }
}