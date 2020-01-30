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
import com.maruchin.medihelper.domain.repositories.SettingsRepo
import java.util.*
import java.util.concurrent.TimeUnit

class DeviceReminderImpl(
    private val context: Context,
    private val settingsRepo: SettingsRepo
) : DeviceReminder {
    private val TAG: String
        get() = "DeviceNotifications"

    companion object {
        private const val NOT_TAKEN_CHECK_INTERVAL_MINUTES = 60L
        private const val NOT_TAKEN_CHECK_WORK_NAME = "not-taken-check-work"
    }

    private val workManager: WorkManager by lazy { WorkManager.getInstance(context) }

    override suspend fun setupPlannedMedicinesReminders() {
        //todo executing UseCase may be a cleaner solution than reference to repository
        val areNotificationsEnabled = settingsRepo.areNotificationsEnabled()
        if (areNotificationsEnabled) {
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
        context.startActivity(intent)
    }

    private fun enablePlannedMedicineCheck() {
        val work = PeriodicWorkRequestBuilder<CheckPlannedMedicinesWorker>(
            repeatInterval = NOT_TAKEN_CHECK_INTERVAL_MINUTES,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).build()
        workManager.enqueueUniquePeriodicWork(
            NOT_TAKEN_CHECK_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )
        Log.i(TAG, "Notifications enabled")
    }

    private fun disablePlannedMedicineCheck() {
        workManager.cancelUniqueWork(NOT_TAKEN_CHECK_WORK_NAME)
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