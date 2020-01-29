package com.maruchin.medihelper.device.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.maruchin.medihelper.domain.device.DeviceNotifications
import com.maruchin.medihelper.domain.model.PlannedMedicineNotifData
import org.koin.core.KoinComponent
import org.koin.core.inject

class NotifyAboutPlannedMedicineWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    companion object {
        const val INPUT_NOTIF_DATA = "input-notif-data"
    }

    private val deviceNotifications: DeviceNotifications by inject()

    override suspend fun doWork(): Result {
        val dataJson = inputData.getString(INPUT_NOTIF_DATA)
        val notifData = Gson().fromJson(dataJson, PlannedMedicineNotifData::class.java)
        deviceNotifications.launchPlannedMedicineNotification(notifData)
        return Result.success()
    }
}