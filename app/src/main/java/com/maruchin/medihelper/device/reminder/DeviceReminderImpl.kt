package com.maruchin.medihelper.device.reminder

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.*
import com.google.gson.Gson
import com.maruchin.medihelper.device.reminder.alarm.AlarmActivity
import com.maruchin.medihelper.device.reminder.notifications.NotTakenMedicineNotification
import com.maruchin.medihelper.device.reminder.notifications.PlannedMedicineNotification
import com.maruchin.medihelper.device.reminder.workers.CheckPlannedMedicinesWorker
import com.maruchin.medihelper.device.reminder.workers.NotifyAboutPlannedMedicineWorker
import com.maruchin.medihelper.domain.device.DeviceReminder
import com.maruchin.medihelper.domain.model.PlannedMedicineNotifData
import com.maruchin.medihelper.domain.usecases.settings.AreRemindersEnabledUseCase
import java.util.*
import java.util.concurrent.TimeUnit

class DeviceReminderImpl(
    private val context: Context,
    private val areRemindersEnabledUseCase: AreRemindersEnabledUseCase
) : DeviceReminder {
    private val TAG: String
        get() = "DeviceNotifications"

    companion object {
        private const val PLANNED_MEDICINE_CHECK_INTERVAL_MINUTES = 60L
        private const val PLANNED_MEDICINE_CHECK_WORK = "planned-medicine-check-work"
    }

    private val workManager: WorkManager by lazy { WorkManager.getInstance(context) }

    override suspend fun setupPlannedMedicinesReminders() {
        val areRemindersEnabled = areRemindersEnabledUseCase.execute()
        if (areRemindersEnabled) {
            enablePlannedMedicineCheck()
        } else {
            disablePlannedMedicineCheck()
        }
    }

    override suspend fun schedulePlannedMedicineReminder(data: PlannedMedicineNotifData) {
        val delay = calculateTimeToNotif(data)
        val dataJson = Gson().toJson(data)
        val inputData = workDataOf(
            NotifyAboutPlannedMedicineWorker.INPUT_NOTIF_DATA to dataJson
        )
        val work = OneTimeWorkRequestBuilder<NotifyAboutPlannedMedicineWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()
        workManager.enqueueUniqueWork(
            data.plannedMedicineId,
            ExistingWorkPolicy.REPLACE,
            work
        )
    }

    override suspend fun launchNotTakenMedicineNotification(data: PlannedMedicineNotifData) {
        val notification =
            NotTakenMedicineNotification(
                context,
                data
            )
        notification.launch()
    }

    override suspend fun launchPlannedMedicineNotification(data: PlannedMedicineNotifData) {
        val notification =
            PlannedMedicineNotification(
                context,
                data
            )
        notification.launch()
    }

    override suspend fun launchPlannedMedicineAlarm(dataJson: String) {
        val intent = Intent(context, AlarmActivity::class.java)
        intent.putExtra(AlarmActivity.EXTRA_DATA, dataJson)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun enablePlannedMedicineCheck() {
        val work = PeriodicWorkRequestBuilder<CheckPlannedMedicinesWorker>(
            repeatInterval = PLANNED_MEDICINE_CHECK_INTERVAL_MINUTES,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).build()
        workManager.enqueueUniquePeriodicWork(
            PLANNED_MEDICINE_CHECK_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )
        Log.i(TAG, "Notifications enabled")
    }

    private fun disablePlannedMedicineCheck() {
        workManager.cancelUniqueWork(PLANNED_MEDICINE_CHECK_WORK)
        Log.i(TAG, "Notifications disabled")
    }

    private fun calculateTimeToNotif(data: PlannedMedicineNotifData): Long {
        val currCalendar = Calendar.getInstance()
        val plannedCalendar = Calendar.getInstance().apply {
            set(
                data.plannedDate.year,
                data.plannedDate.month - 1,
                data.plannedDate.day,
                data.plannedTime.hour,
                data.plannedTime.minute,
                0
            )
        }
        var millisDiff = plannedCalendar.timeInMillis - currCalendar.timeInMillis
        if (millisDiff < 0) {
            millisDiff = 0
        }
        return millisDiff
    }
}