package com.maruchin.medihelper.device.reminder

import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.PlannedMedicineNotfiData
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetPlannedMedicineNotifDataUseCase
import com.maruchin.medihelper.presentation.feature.alarm.AlarmActivity
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

            if (data.status == PlannedMedicine.Status.NOT_TAKEN) {
                launchAlarm(data)
//                showNotification(data)
            }
        }

        return Result.success()
    }

    private fun showNotification(data: PlannedMedicineNotfiData) {
        PlannedMedicineNotification(
            context = context,
            plannedMedicineId = data.plannedMedicineId,
            profileName = data.profileName,
            profileColor = data.profileColor,
            medicineName = data.medicineName,
            medicineUnit = data.medicineUnit,
            plannedTime = data.plannedTime,
            doseSize = data.doseSize
        ) .show()
    }

    private fun launchAlarm(data: PlannedMedicineNotfiData) {
        val intent = Intent(context, AlarmActivity::class.java).apply {
            putExtra(AlarmActivity.EXTRA_NOTIF_DATA, data)
        }

        context.startActivity(intent)
    }
}