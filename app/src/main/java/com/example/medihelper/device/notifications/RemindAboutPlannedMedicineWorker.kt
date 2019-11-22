package com.example.medihelper.device.notifications

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.medihelper.data.local.dao.PlannedMedicineDao
import com.example.medihelper.domain.usecases.PlannedMedicineUseCases
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

    private val plannedMedicineUseCases: PlannedMedicineUseCases by inject()
    private val medicineReminderNotif: MedicineReminderNotif by inject()

    override suspend fun doWork(): Result {
        Log.i(TAG, "doWork")

        val plannedMedicineId = inputData.getInt(INPUT_PLANNED_MEDICINE_ID, -1)
        if (plannedMedicineId != -1) {
            val data = plannedMedicineUseCases.getPlannedMedicineWithMedicineAndPersonById(plannedMedicineId)
            medicineReminderNotif.notify(
                personName = data.person.name,
                personColorResId = data.person.colorId,
                medicineName = data.medicine.name,
                plannedTime = data.plannedTime
            )
        }

        return Result.success()
    }
}