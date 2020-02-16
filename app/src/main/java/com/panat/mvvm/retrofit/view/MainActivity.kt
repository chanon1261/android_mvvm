package com.panat.mvvm.retrofit.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.panat.mvvm.retrofit.R
import com.panat.mvvm.retrofit.databinding.ActivityMainBinding
import com.panat.mvvm.retrofit.viewModel.MainViewModel
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by inject()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
    }
}
