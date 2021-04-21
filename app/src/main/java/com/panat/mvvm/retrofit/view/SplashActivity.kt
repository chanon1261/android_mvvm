package com.panat.mvvm.retrofit.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.panat.mvvm.retrofit.R
import com.panat.mvvm.retrofit.base.BaseActivity
import com.panat.mvvm.retrofit.databinding.ActivitySplashBinding


class SplashActivity : BaseActivity<ActivitySplashBinding>() {


    private val timeOut = 2000L // 1.5 sec

    override fun initView() {
        bindView(ActivitySplashBinding.inflate(layoutInflater))
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, timeOut)
    }
}
