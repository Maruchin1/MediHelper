package com.example.medihelper.device.notifications

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.medihelper.data.local.dao.PlannedMedicineDao
import org.koin.core.KoinComponent
import org.koin.core.inject

class RemindAboutPlannedMedicineWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {
    private val TAG = "RemindMedMedWorker"

    companion object {
        const val INPUT_PLANNED_MEDICINE_ID = "input-planned-medicine-id"
    }

    private val plannedMedicineDao: PlannedMedicineDao by inject()
    private val notificationUtil: NotificationUtil by inject()

    override suspend fun doWork(): Result {
        Log.i(TAG, "doWork")

        val plannedMedicineId = inputData.getInt(INPUT_PLANNED_MEDICINE_ID, -1)
        if (plannedMedicineId != -1) {
            //todo pobierać plannedMedicine i mapować na wewnętrzy model remindera
//            val reminderData = plannedMedicineDao.getReminder(plannedMedicineId)
//            notificationUtil.showReminderNotification(
//                personName = reminderData.personName,
//                personColorResId = reminderData.personColorResId,
//                medicineName = reminderData.medicineName,
//                plannedTime = reminderData.plannedTime
//            )
        }

        return Result.success()
    }
}