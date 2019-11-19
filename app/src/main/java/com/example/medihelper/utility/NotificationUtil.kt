package com.example.medihelper.utility

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.medihelper.R
import com.example.medihelper.localdata.type.AppTime

class NotificationUtil(private val context: Context) {

    companion object {
        private const val SERVER_SYNC_NOTIFICATION_ID = 1
        private const val SERVER_SYNC_FAILURE_NOTIFICATION_ID = 2
        private const val SERVER_SYNC_CHANNEL_ID = "server-sync-channel-id"
        private const val SERVER_SYNC_CHANNEL_NAME = "MediHelper server sync notification channel"

        private const val REMINDER_PLANNED_MEDICINE_NOTIFICATION_ID = 3
        private const val REMINDER_CHANNEL_ID = "reminder-channel-id"
        private const val REMINDER_CHANNEL_NAME = "reminder-channel-name"
    }

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private val notificationManagerCompat by lazy {
        NotificationManagerCompat.from(context)
    }
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
            .setContentText("Podczas synchronizacji danych wystąpił błąd")
            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }


    init {
        createServerSyncNotificationChannel()
        createReminderNotificationChannel()
    }

    fun showServerSyncNotification() =
        notificationManagerCompat.notify(SERVER_SYNC_NOTIFICATION_ID, serverSyncNotification)

    fun showServerSyncFailNotification() =
        notificationManagerCompat.notify(SERVER_SYNC_FAILURE_NOTIFICATION_ID, serverSyncFailureNotification)

    fun showReminderNotification(
        personName: String,
        personColorResId: Int,
        medicineName: String,
        plannedTime: AppTime
    ) = notificationManagerCompat.notify(
        REMINDER_PLANNED_MEDICINE_NOTIFICATION_ID,
        getPlannedMedicineReminderNotification(personName, personColorResId, medicineName, plannedTime)
    )

    fun cancelServerSyncNotification() =
        notificationManagerCompat.cancel(SERVER_SYNC_NOTIFICATION_ID)

    private fun getPlannedMedicineReminderNotification(
        personName: String,
        personColorResId: Int,
        medicineName: String,
        plannedTime: AppTime
    ): Notification {
        return NotificationCompat.Builder(context, REMINDER_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("$personName, pora przyjąć lek!")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "Na godzinę ${plannedTime.formatString} zaplanowano przyjęcie leku $medicineName"
                )
            )
            .setColor(ContextCompat.getColor(context, personColorResId))
            .build()
    }

    private fun createServerSyncNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                SERVER_SYNC_CHANNEL_ID,
                SERVER_SYNC_CHANNEL_NAME,
                importance
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createReminderNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                REMINDER_CHANNEL_ID,
                REMINDER_CHANNEL_NAME,
                importance
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}