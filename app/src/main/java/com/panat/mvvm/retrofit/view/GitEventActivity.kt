package com.panat.mvvm.retrofit.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.panat.mvvm.retrofit.R
import com.panat.mvvm.retrofit.adapter.GitEventsAdapter
import com.panat.mvvm.retrofit.viewModel.GitEventActivityViewModel
import kotlinx.android.synthetic.main.activity_git_event.*
import org.koin.android.ext.android.inject

class GitEventActivity : AppCompatActivity() {

    private val viewModel: GitEventActivityViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_git_event)
        val adapter = GitEventsAdapter(this)
        rvEvent.layoutManager = LinearLayoutManager(this)
        rvEvent.adapter = adapter

        viewModel.loadEvents()
        viewModel.events.observe(this, androidx.lifecycle.Observer {
            adapter.loadData(it)
        })

        viewModel.events2.observe(this, androidx.lifecycle.Observer {
            adapter.add(it)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadEvents()
    }
}

