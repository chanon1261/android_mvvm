package com.panat.mvvm.retrofit.di

import com.panat.mvvm.retrofit.viewModel.GitEventViewModel
import com.panat.mvvm.retrofit.viewModel.MainViewModel
import com.panat.mvvm.retrofit.viewModel.SocketViewModel
import com.panat.mvvm.retrofit.viewModel.UpLoadViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { GitEventViewModel() }
    viewModel { MainViewModel(get()) }
    viewModel { UpLoadViewModel() }
    viewModel { SocketViewModel() }
}

