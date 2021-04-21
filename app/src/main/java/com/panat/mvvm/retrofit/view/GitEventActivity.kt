package com.panat.mvvm.retrofit.view

import androidx.recyclerview.widget.LinearLayoutManager
import com.panat.mvvm.retrofit.adapter.GitEventsAdapter
import com.panat.mvvm.retrofit.base.BaseActivity
import com.panat.mvvm.retrofit.databinding.ActivityGitEventBinding
import com.panat.mvvm.retrofit.di.provideSwitcher
import com.panat.mvvm.retrofit.viewModel.GitEventViewModel
import kotlinx.android.synthetic.main.activity_git_event.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import org.koin.android.ext.android.inject

class GitEventActivity : BaseActivity<ActivityGitEventBinding>() {

    private val viewModel: GitEventViewModel by inject()
    private lateinit var adapter: GitEventsAdapter

    override fun initView() {
        bindView(ActivityGitEventBinding.inflate(layoutInflater))

        title = "Git Event"
        val switch = provideSwitcher(binding.rvEvent, emptyView, errorView, progressView, this)

        adapter = GitEventsAdapter(this)
        rvEvent.layoutManager = LinearLayoutManager(this)
        rvEvent.adapter = adapter

        switch.showProgressView()
        viewModel.events.observe(this, androidx.lifecycle.Observer {
            it?.let {
                println("GithubEvents $it")
                adapter.loadData(it)
                switch.showContentView()
            } ?: run {
                switch.showErrorView()
            }
        })
    }

}

