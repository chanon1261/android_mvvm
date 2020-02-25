package com.panat.mvvm.retrofit.viewModel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.panat.mvvm.retrofit.R
import com.panat.mvvm.retrofit.view.*
import me.leolin.shortcutbadger.ShortcutBadger

class MainViewModel(private val context: Context) : ViewModel() {

    fun go(position: Int) {
        when (position) {
            0 -> {
                goTo(GitEventActivity::class.java)
            }
            1 -> {
                goTo(SocketActivity::class.java)
            }
            2 -> {
                goTo(UploadActivity::class.java)
            }
            3 -> {
                goTo(FirebaseCloudMessagingActivity::class.java)
            }
            4 -> {
                goTo(ReadWriteFolderActivity::class.java)
            }
            else -> {

            }
        }
    }

    private fun goTo(cls: Class<*>?) {
        val intent = Intent(context, cls)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun sendNotification(count: Int) {
        val channelId = context.getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val notification = NotificationCompat.Builder(context, "$channelId$count")
            .setSmallIcon(R.drawable.ic_favorite_black_24dp)
            .setContentTitle(context.getString(R.string.fcm_message) + count)
            .setContentText("test$count")
            .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setNumber(count)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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

        if (ShortcutBadger.isBadgeCounterSupported(context)) {
            ShortcutBadger.applyNotification(context, notification, count)
        }
    }
}