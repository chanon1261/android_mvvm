package com.panat.mvvm.retrofit.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.BADGE_ICON_LARGE
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.panat.mvvm.retrofit.R
import com.panat.mvvm.retrofit.adapter.MenuAdapter
import com.panat.mvvm.retrofit.databinding.ActivityMainBinding
import com.panat.mvvm.retrofit.utils.Constants
import com.panat.mvvm.retrofit.viewModel.MainViewModel
import me.leolin.shortcutbadger.ShortcutBadger
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by inject()
    private lateinit var binding: ActivityMainBinding
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        val adapter = MenuAdapter(this, object : MenuAdapter.OnClickItem {
            override fun onclick(position: Int) {
                viewModel.go(position)
            }
        })
        binding.rvMenu.layoutManager = LinearLayoutManager(this)
        binding.rvMenu.adapter = adapter
        adapter.loadData(Constants.menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                sendNotification()
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun sendNotification() {

        count++
        println(
            "ShortcutBadger.isBadgeCounterSupported  ${ShortcutBadger.isBadgeCounterSupported(
                this
            )}"
        )

        if (ShortcutBadger.isBadgeCounterSupported(this)) {
            ShortcutBadger.applyCount(this, 122)
        }

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val notification = NotificationCompat.Builder(this, "$channelId$count")
            .setSmallIcon(R.drawable.ic_favorite_black_24dp)
            .setContentTitle(getString(R.string.fcm_message))
            .setContentText("test$count")
            .setBadgeIconType(BADGE_ICON_LARGE)
            .setNumber(1)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .build()


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "$channelId$count",
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.setShowBadge(true)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0/* ID of notification */, notification)

        startActivity(Intent(this, SplashActivity::class.java))
    }
}
