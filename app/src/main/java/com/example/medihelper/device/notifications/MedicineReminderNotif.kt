package com.example.medihelper.device.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.medihelper.R
import com.example.medihelper.domain.entities.AppTime

class MedicineReminderNotif(private val context: Context) {

    companion object {
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

    init {
        createReminderNotificationChannel()
    }

    fun notify(
        personName: String,
        personColorResId: Int,
        medicineName: String,
        plannedTime: AppTime
    ) = notificationManagerCompat.notify(
        REMINDER_PLANNED_MEDICINE_NOTIFICATION_ID,
        getPlannedMedicineReminderNotification(personName, personColorResId, medicineName, plannedTime)
    )

    private fun getPlannedMedicineReminderNotification(
        personName: String,
        personColorResId: Int,
        medicineName: String,
        plannedTime: AppTime
    ): Notification {
        return NotificationCompat.Builder(context,
            REMINDER_CHANNEL_ID
        )
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