package com.maruchin.medihelper.device.reminder

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.presentation.LauncherActivity

class PlannedMedicineNotification(
    private val context: Context,
    private val plannedMedicineId: String,
    profileName: String,
    profileColor: String,
    medicineName: String,
    medicineUnit: String,
    plannedTime: String,
    doseSize: Float
) : BaseNotification(context) {

    companion object {
        const val ACTION_SET_MEDICINE_TAKEN = "action-set-medicine-taken"
        const val ACTION_OPEN_APP = "action-open-app"
        private const val NOTIFICATION_ID = 0
        private const val REQUEST_CODE_MEDICINE_TAKEN = 1
        private const val REQUEST_CODE_OPEN_APP = 2

        fun cancel(plannedMedicineId: String, context: Context) {
            val manager = NotificationManagerCompat.from(context)
            manager.cancel(plannedMedicineId, NOTIFICATION_ID)
        }
    }

    override val channelId: String
        get() = "planned-medicine-notification-channel-id"
    override val channelName: String
        get() = "planned-medicine-notification-channel-name"

    private val notification: Notification

    init {
        notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("$profileName, pora przyjąć lek!")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "$plannedTime - $medicineName $doseSize $medicineUnit"
                )
            )
            .setColor(Color.parseColor(profileColor))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(
                R.drawable.baseline_check_24,
                "Przyjęty",
                getMedicineTakenPendingIntent(plannedMedicineId)
            )
            .addAction(
                R.drawable.ic_pill_black_24dp,
                "Szczegóły",
                getOpenAppPendingIntent()
            )
            .build()
    }

    fun show() {
        val manager = NotificationManagerCompat.from(context)
        manager.notify(plannedMedicineId, NOTIFICATION_ID, notification)
    }

    private fun getMedicineTakenPendingIntent(plannedMedicineId: String): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            action = ACTION_SET_MEDICINE_TAKEN
            data = plannedMedicineId.toUri()
        }

        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_MEDICINE_TAKEN,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getOpenAppPendingIntent(): PendingIntent {
        val intent = Intent(context, LauncherActivity::class.java).apply {
            action = ACTION_OPEN_APP
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        return PendingIntent.getActivity(
            context,
            REQUEST_CODE_OPEN_APP,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}