package com.panat.mvvm.retrofit.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.panat.mvvm.retrofit.R
import com.panat.mvvm.retrofit.databinding.ActivityMainBinding
import com.panat.mvvm.retrofit.viewModel.MainViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
    }
}
