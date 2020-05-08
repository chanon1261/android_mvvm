package com.panat.mvvm.retrofit.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.panat.mvvm.retrofit.R
import com.panat.mvvm.retrofit.adapter.MenuAdapter
import com.panat.mvvm.retrofit.databinding.ActivityMainBinding
import com.panat.mvvm.retrofit.utils.Constants
import com.panat.mvvm.retrofit.viewModel.MainViewModel
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by inject()
    private lateinit var binding: ActivityMainBinding
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

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
