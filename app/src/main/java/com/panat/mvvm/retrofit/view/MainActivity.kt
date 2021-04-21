package com.panat.mvvm.retrofit.view

import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.panat.mvvm.retrofit.R
import com.panat.mvvm.retrofit.adapter.MenuAdapter
import com.panat.mvvm.retrofit.base.BaseActivity
import com.panat.mvvm.retrofit.databinding.ActivityMainBinding
import com.panat.mvvm.retrofit.utils.Constants
import com.panat.mvvm.retrofit.viewModel.MainViewModel
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel: MainViewModel by inject()
    private var count = 0

    override fun initView() {
        bindView(ActivityMainBinding.inflate(layoutInflater))
        initRecyclerview()
    }

    private fun initRecyclerview() {
        val adapter = MenuAdapter(this) { position ->
            viewModel.go(position)
        }

        binding.rvMenu.layoutManager = LinearLayoutManager(this)
        binding.rvMenu.adapter = adapter
        adapter.loadData(Constants.menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                ++count
                viewModel.sendNotification(count)
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
