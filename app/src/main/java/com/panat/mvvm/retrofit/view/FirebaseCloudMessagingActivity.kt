package com.panat.mvvm.retrofit.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.panat.mvvm.retrofit.R
import com.panat.mvvm.retrofit.base.BaseActivity
import com.panat.mvvm.retrofit.databinding.ActivityFirebaseCloudMessagingBinding
import kotlinx.android.synthetic.main.activity_firebase_cloud_messaging.*

class FirebaseCloudMessagingActivity : BaseActivity<ActivityFirebaseCloudMessagingBinding>() {

    override fun initView() {
        bindView(ActivityFirebaseCloudMessagingBinding.inflate(layoutInflater))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                    NotificationChannel(channelId,
                            channelName, NotificationManager.IMPORTANCE_LOW)
            )
        }

        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        // [START handle_data_extras]
        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                Log.d(TAG, "Key: $key Value: $value")
            }
        }
        // [END handle_data_extras]

        subscribeButton.setOnClickListener {
            Log.d(TAG, "Subscribing to weather topic")
            // [START subscribe_topics]
            FirebaseMessaging.getInstance().subscribeToTopic("weather")
                    .addOnCompleteListener { task ->
                        var msg = getString(R.string.msg_subscribed)
                        if (!task.isSuccessful) {
                            msg = getString(R.string.msg_subscribe_failed)
                        }
                        Log.d(TAG, msg)
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    }
            // [END subscribe_topics]
        }

        logTokenButton.setOnClickListener {
            // Get token
            // [START retrieve_current_token]
            FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w(TAG, "getInstanceId failed", task.exception)
                            return@OnCompleteListener
                        }

                        // Get new Instance ID token
                        val token = task.result?.token

                        // Log and toast
                        val msg = getString(R.string.msg_token_fmt, token)
                        Log.d(TAG, msg)
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    })
            // [END retrieve_current_token]
        }

        Toast.makeText(this, "See README for setup instructions", Toast.LENGTH_SHORT).show()
    }


    companion object {
        private const val TAG = "FCM ACTIVITY"
    }

}
