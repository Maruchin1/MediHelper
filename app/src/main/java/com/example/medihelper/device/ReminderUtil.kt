package com.example.medihelper.device

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf

class ReminderUtil(private val context: Context) {

    private val workManager: WorkManager by lazy { WorkManager.getInstance(context) }

    fun updateReminders() {
        val work = OneTimeWorkRequestBuilder<UpdateRemindersWorker>().build()
        workManager.enqueue(work)
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