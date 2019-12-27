package com.maruchin.medihelper.device.reminder

import android.app.Notification
import android.content.Context
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.AppTime

class PlannedMedicineNotification(
    private val context: Context,
    private val plannedMedicineId: String,
    profileName: String,
    profileColor: String,
    medicineName: String,
    plannedTime: AppTime
) : BaseNotification(context) {

    override val notificationId: Int
        get() = 0
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
                    "Na godzinę ${plannedTime.formatString} zaplanowano przyjęcie leku $medicineName"
                )
            )
            .setColor(Color.parseColor(profileColor))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    fun show() {
        val manager = NotificationManagerCompat.from(context)
        manager.notify(plannedMedicineId, notificationId, notification)
    }
}