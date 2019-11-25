package com.example.medihelper.device.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.medihelper.R
import com.example.medihelper.domain.entities.AppTime
import com.example.medihelper.presentation.feature.MainActivity

class MedicineReminderNotification(private val context: Context) {

    companion object {
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
        plannedMedicineId: Int,
        personName: String,
        personColorResId: Int,
        medicineName: String,
        plannedTime: AppTime
    ) = notificationManagerCompat.notify(
        getNotificationId(plannedMedicineId),
        getPlannedMedicineReminderNotification(
            plannedMedicineId,
            personName,
            personColorResId,
            medicineName,
            plannedTime
        )
    )

    fun cancel(plannedMedicineId: Int) = notificationManagerCompat.cancel(getNotificationId(plannedMedicineId))

    private fun getPlannedMedicineReminderNotification(
        plannedMedicineId: Int,
        personName: String,
        personColorResId: Int,
        medicineName: String,
        plannedTime: AppTime
    ): Notification {
        return NotificationCompat.Builder(
            context,
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
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getContentIntent())
            .addAction(
                R.drawable.baseline_check_24,
                "Przyjęty",
                getMedicineTakenIntent(plannedMedicineId, medicineTaken = true)
            )
            .addAction(
                R.drawable.round_close_24,
                "Nieprzyjęty",
                getMedicineTakenIntent(plannedMedicineId, medicineTaken = false)
            )
            .build()
    }

    private fun getNotificationId(plannedMedicineId: Int) = 100 + plannedMedicineId

    private fun getContentIntent(): PendingIntent {
        return Intent(context, MainActivity::class.java).let { intent ->
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            PendingIntent.getActivity(context, 0, intent, 0)
        }
    }

    private fun getMedicineTakenIntent(plannedMedicineId: Int, medicineTaken: Boolean): PendingIntent {
        return Intent(context, MedicineTakenReceiver::class.java).let { intent ->
            intent.putExtra(MedicineTakenReceiver.EXTRA_PLANNED_MEDICINE_ID, plannedMedicineId)
            intent.putExtra(MedicineTakenReceiver.EXTRA_MEDICINE_TAKEN, medicineTaken)
            PendingIntent.getBroadcast(context, plannedMedicineId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
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