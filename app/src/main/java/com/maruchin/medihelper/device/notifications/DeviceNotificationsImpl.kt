package com.maruchin.medihelper.device.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.maruchin.medihelper.domain.device.DeviceNotifications
import com.maruchin.medihelper.domain.model.PlannedMedicineNotifData
import com.maruchin.medihelper.domain.repositories.SettingsRepo
import java.util.concurrent.TimeUnit

class DeviceNotificationsImpl(
    private val context: Context,
    private val settingsRepo: SettingsRepo
) : DeviceNotifications {

    companion object {
        private const val NOT_TAKEN_CHECK_INTERVAL_MINUTES = 60L
        private const val NOT_TAKEN_CHECK_WORK_NAME = "not-taken-check-work"
    }

    private val workManager: WorkManager by lazy { WorkManager.getInstance(context) }

    override suspend fun setupNotTakenMedicinesChecking() {
        val areNotificationsEnabled = settingsRepo.areNotificationsEnabled()
        if (areNotificationsEnabled) {
            enableNotTakenMedicineCheck()
        } else {
            disableNotTakenMedicineCheck()
        }
    }

    override suspend fun launchPlannedMedicineNotification(data: PlannedMedicineNotifData) {
        val notification = NotTakenMedicineNotification(context, data)
        notification.launch()
    }

    private fun enableNotTakenMedicineCheck() {
        val work = PeriodicWorkRequestBuilder<CheckNotTakenMedicinesWorker>(
            repeatInterval = NOT_TAKEN_CHECK_INTERVAL_MINUTES,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).build()
        workManager.enqueueUniquePeriodicWork(
            NOT_TAKEN_CHECK_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )
    }

    private fun disableNotTakenMedicineCheck() {
        workManager.cancelUniqueWork(NOT_TAKEN_CHECK_WORK_NAME)
    }
}