package com.example.medihelper.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.medihelper.R

class NotificationService(context: Context) {

    companion object {
        private const val SERVER_SYNC_NOTIFICATION_ID = 1
        private const val SERVER_SYNC_FAILURE_NOTIFICATION_ID = 2
        private const val SERVER_SYNC_CHANNEL_ID = "server-sync-channel-id"
        private const val SERVER_SYNC_CHANNEL_NAME = "MediHelper server sync notification channel"
    }

    private val notificationManagerCompat by lazy { NotificationManagerCompat.from(context) }
    private val serverSyncNotification by lazy {
        NotificationCompat.Builder(context, SERVER_SYNC_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Synchronizacja danych")
            .setOngoing(true)
            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setProgress(0, 0, true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
    private val serverSyncFailureNotification by lazy {
        NotificationCompat.Builder(context, SERVER_SYNC_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Synchronizacja danych nieudana")
            .setContentText("Podczas synchronizacji danych wystąpił problem. Sprawdź połączenie z internetem.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    init {
        createServerSyncNotificationChannel(context)
    }

    fun showServerSyncNotification() = notificationManagerCompat.notify(SERVER_SYNC_NOTIFICATION_ID, serverSyncNotification)

    fun showServerSyncFailureNotification() =
        notificationManagerCompat.notify(SERVER_SYNC_FAILURE_NOTIFICATION_ID, serverSyncFailureNotification)

    fun cancelServerSyncNotification() = notificationManagerCompat.cancel(SERVER_SYNC_NOTIFICATION_ID)

    private fun createServerSyncNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(SERVER_SYNC_CHANNEL_ID, SERVER_SYNC_CHANNEL_NAME, importance)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}