package com.maruchin.medihelper.device.reminder.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.maruchin.medihelper.domain.device.DeviceReminder
import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.model.PlannedMedicineNotifData
import com.maruchin.medihelper.domain.usecases.settings.AreRemindersEnabledUseCase
import com.maruchin.medihelper.domain.usecases.settings.GetReminderModeUseCase
import org.koin.core.KoinComponent
import org.koin.core.inject

class NotifyAboutPlannedMedicineWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    companion object {
        const val INPUT_NOTIF_DATA = "input-notif-data"
    }

    private val deviceReminder: DeviceReminder by inject()
    private val areRemindersEnabledUseCase: AreRemindersEnabledUseCase by inject()
    private val getReminderModeUseCase: GetReminderModeUseCase by inject()

    override suspend fun doWork(): Result {
        val dataJson = inputData.getString(INPUT_NOTIF_DATA)
        if (dataJson != null) {
            notifyAccordingToSettings(dataJson)
        }
        return Result.success()
    }

    private suspend fun notifyAccordingToSettings(dataJson: String) {
        val remindersEnabled = areRemindersEnabledUseCase.execute()
        if (remindersEnabled) {
            val mode = getReminderModeUseCase.execute()
            when (mode) {
                ReminderMode.NOTIFICATIONS -> launchNotification(dataJson)
                ReminderMode.ALARMS -> launchAlarms(dataJson)
            }
        }
    }

    private suspend fun launchNotification(dataJson: String) {
        val notifData = Gson().fromJson(dataJson, PlannedMedicineNotifData::class.java)
        deviceReminder.launchPlannedMedicineNotification(notifData)
    }

    private suspend fun launchAlarms(dataJson: String) {
        deviceReminder.launchPlannedMedicineAlarm(dataJson)
    }
}