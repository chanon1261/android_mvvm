package com.panat.mvvm.retrofit.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.panat.mvvm.retrofit.R
import com.panat.mvvm.retrofit.adapter.GitEventsAdapter
import com.panat.mvvm.retrofit.viewModel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adpater = GitEventsAdapter(this)
        rvEvent.layoutManager = LinearLayoutManager(this)
        rvEvent.adapter = adpater

        viewModel.loadEvents()
        viewModel.events.observe(this, androidx.lifecycle.Observer {
            adpater.loadData(it)
        })

        viewModel.events2.observe(this, androidx.lifecycle.Observer {
            adpater.add(it)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadEvents()
    }
}

