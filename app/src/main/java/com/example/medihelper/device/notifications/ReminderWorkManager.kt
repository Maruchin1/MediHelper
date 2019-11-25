package com.example.medihelper.device.notifications

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.medihelper.device.deviceapi.NotificationApiImpl

class ReminderWorkManager(private val context: Context) {

    companion object {
        private const val UPDATE_REMINDERS_WORK_NAME = "update-reminders-work-name"
    }

    private val workManager: WorkManager by lazy { WorkManager.getInstance(context) }

    fun updateReminders() {
        val work = OneTimeWorkRequestBuilder<UpdateRemindersWorker>().build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            UPDATE_REMINDERS_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            work
        )
    }

    fun remindAboutPlannedMedicine(plannedMedicineId: Int) {
        val work = OneTimeWorkRequestBuilder<RemindAboutPlannedMedicineWorker>()
            .setInputData(
                workDataOf(
                    RemindAboutPlannedMedicineWorker.INPUT_PLANNED_MEDICINE_ID to plannedMedicineId
                )
            )
            .build()
        workManager.enqueue(work)
    }
}