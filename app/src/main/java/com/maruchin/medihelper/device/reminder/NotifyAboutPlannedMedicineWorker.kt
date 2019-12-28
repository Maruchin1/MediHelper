package com.maruchin.medihelper.device.reminder

import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.PlannedMedicineNotifData
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetPlannedMedicineNotifDataUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.NotifyAboutPlannedMedicineUseCase
import com.maruchin.medihelper.presentation.feature.alarm.AlarmActivity
import org.koin.core.KoinComponent
import org.koin.core.inject

class NotifyAboutPlannedMedicineWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    companion object {
        const val INPUT_PLANNED_MEDICINE_ID = "input-planned-medicine-id"
    }

    private val notifyAboutPlannedMedicineUseCase: NotifyAboutPlannedMedicineUseCase by inject()

    override suspend fun doWork(): Result {
        inputData.getString(INPUT_PLANNED_MEDICINE_ID)?.let { plannedMedicineId ->
            notifyAboutPlannedMedicineUseCase.execute(plannedMedicineId)
        }

        return Result.success()
    }

}