package com.panat.mvvm.retrofit.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.panat.mvvm.retrofit.R
import com.panat.mvvm.retrofit.adapter.GitEventsAdapter
import com.panat.mvvm.retrofit.databinding.ActivityGitEventBinding
import com.panat.mvvm.retrofit.viewModel.GitEventViewModel
import kotlinx.android.synthetic.main.activity_git_event.*
import org.koin.android.ext.android.inject

class GitEventActivity : BaseActivity() {

    private val viewModel: GitEventViewModel by inject()
    private lateinit var binding: ActivityGitEventBinding
    private lateinit var adapter: GitEventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = GitEventsAdapter(this)
        rvEvent.layoutManager = LinearLayoutManager(this)
        rvEvent.adapter = adapter

        viewModel.events.observe(this, androidx.lifecycle.Observer {
            println("GithubEvents $it")
            adapter.loadData(it)
        })
        viewModel.start()
    }

    override fun setupView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_git_event)
        binding.lifecycleOwner = this
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Git Event"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}

