package com.maruchin.medihelper.device.reminder

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetPlannedMedicineNotifDataUseCase
import org.koin.core.KoinComponent
import org.koin.core.inject

class PlannedMedicineWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    companion object {
        const val INPUT_PLANNED_MEDICINE_ID = "input-planned-medicine-id"
    }

    private val getPlannedMedicineNotifDataUseCase: GetPlannedMedicineNotifDataUseCase by inject()

    override suspend fun doWork(): Result {

        inputData.getString(INPUT_PLANNED_MEDICINE_ID)?.let { plannedMedicineId ->
            val data = getPlannedMedicineNotifDataUseCase.execute(plannedMedicineId)
            PlannedMedicineNotification(
                context = context,
                plannedMedicineId = data.plannedMedicineId,
                profileName = data.profileName,
                profileColor = data.profileColor,
                medicineName = data.medicineName,
                plannedTime = data.plannedTime
            ) .show()
        }

        return Result.success()
    }
}