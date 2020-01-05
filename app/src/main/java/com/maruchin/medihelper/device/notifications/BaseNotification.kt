package com.maruchin.medihelper.device.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import org.koin.core.KoinComponent

abstract class BaseNotification(private val context: Context) : KoinComponent {

    protected abstract val channelId: String
    protected abstract val channelName: String

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                channelId,
                channelName,
                importance
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}