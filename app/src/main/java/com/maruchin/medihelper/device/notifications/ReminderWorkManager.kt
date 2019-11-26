package com.maruchin.medihelper.device.notifications

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

class ReminderWorkManager(private val context: Context) {

    companion object {
        private const val PERIODIC_UPDATE_REMINDERS_WORK_NAME = "periodic-update-reminders-work-name"
        private const val UPDATE_REMINDERS_WORK_NAME = "update-reminders-work-name"
    }

    private val workManager: WorkManager by lazy { WorkManager.getInstance(context) }

    fun enablePeriodicRemindersUpdate() {
        val currDate = Calendar.getInstance()
        val updateDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 10)
            set(Calendar.SECOND, 0)
        }

        if (updateDate.before(currDate)) {
            updateDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = updateDate.timeInMillis - currDate.timeInMillis
        val work = OneTimeWorkRequestBuilder<UpdateRemindersWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    UpdateRemindersWorker.ENABLE_PERIODIC_UPDATE to true
                )
            )
            .build()
        workManager.enqueueUniqueWork(
            PERIODIC_UPDATE_REMINDERS_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            work
        )
    }

    fun updateReminders() {
        val work = OneTimeWorkRequestBuilder<UpdateRemindersWorker>().build()
        workManager.enqueueUniqueWork(
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