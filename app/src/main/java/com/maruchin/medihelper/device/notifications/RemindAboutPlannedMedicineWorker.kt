package com.maruchin.medihelper.device.notifications

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maruchin.medihelper.domain.usecases.PlannedMedicineUseCases
import org.koin.core.KoinComponent
import org.koin.core.inject

class RemindAboutPlannedMedicineWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {
    private val TAG = "RemindMedMedWorker"

    companion object {
        const val INPUT_PLANNED_MEDICINE_ID = "input-planned-medicine-id"
    }

    private val plannedMedicineUseCases: PlannedMedicineUseCases by inject()
    private val medicineReminderNotification: MedicineReminderNotification by inject()

    override suspend fun doWork(): Result {
        Log.i(TAG, "doWork")

        val plannedMedicineId = inputData.getInt(INPUT_PLANNED_MEDICINE_ID, -1)
        if (plannedMedicineId != -1) {
//            val data = plannedMedicineUseCases.getPlannedMedicineWithMedicineAndPersonById(plannedMedicineId)
//            medicineReminderNotification.notify(
//                data.plannedMedicineId,
//                profileName = data.person.name,
//                personColorResId = data.person.color,
//                medicineName = data.medicine.name,
//                plannedTime = data.plannedTime
//            )
        }
        return Result.success()
    }
}