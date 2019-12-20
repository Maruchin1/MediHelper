package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.deviceapi.DeviceCalendar
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo

class ChangePlannedMedicineTakenUseCase(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val deviceCalendar: DeviceCalendar
) {
    suspend fun execute(plannedMedicineId: String) {
        val currTimeInMillis = deviceCalendar.getCurrTimeInMillis()
        val currDate = AppDate(currTimeInMillis)
        val currTime = AppTime(currTimeInMillis)

        plannedMedicineRepo.getById(plannedMedicineId)?.let { plannedMedicine ->
            val newStatus = getNewStatus(plannedMedicine, currDate, currTime)
            plannedMedicine.status = newStatus
            plannedMedicineRepo.update(plannedMedicine)
        }
    }

    private fun getNewStatus(plannedMedicine: PlannedMedicine, currDate: AppDate, currTime: AppTime): PlannedMedicine.Status {
        return plannedMedicine.run {
            when (status) {
                PlannedMedicine.Status.PENDING -> PlannedMedicine.Status.TAKEN
                PlannedMedicine.Status.NOT_TAKEN -> PlannedMedicine.Status.TAKEN
                PlannedMedicine.Status.TAKEN -> when {
                    plannedDate < currDate -> PlannedMedicine.Status.NOT_TAKEN
                    plannedDate > currDate -> PlannedMedicine.Status.PENDING
                    else -> when {
                        plannedTime < currTime -> PlannedMedicine.Status.NOT_TAKEN
                        else -> PlannedMedicine.Status.PENDING
                    }
                }
            }
        }
    }
}