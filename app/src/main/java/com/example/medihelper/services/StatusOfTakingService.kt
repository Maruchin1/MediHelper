package com.example.medihelper.services

import com.example.medihelper.localdatabase.entity.PlannedMedicineEntity

class StatusOfTakingService(
    private val dateTimeService: DateTimeService
) {
    fun getStatusByTaken(
        plannedMedicineEntity: PlannedMedicineEntity,
        taken: Boolean
    ): PlannedMedicineEntity.StatusOfTaking {
        return if (taken) {
            PlannedMedicineEntity.StatusOfTaking.TAKEN
        } else {
            getStatusOfTakingByCurrDate(plannedMedicineEntity)
        }
    }

    fun getStatusByCurrDate(plannedMedicineEntity: PlannedMedicineEntity): PlannedMedicineEntity.StatusOfTaking {
        return if (plannedMedicineEntity.statusOfTaking == PlannedMedicineEntity.StatusOfTaking.TAKEN) {
            PlannedMedicineEntity.StatusOfTaking.TAKEN
        } else {
            getStatusOfTakingByCurrDate(plannedMedicineEntity)
        }
    }

    private fun getStatusOfTakingByCurrDate(plannedMedicineEntity: PlannedMedicineEntity): PlannedMedicineEntity.StatusOfTaking {
        val currDate = dateTimeService.getCurrDate()
        val currTime = dateTimeService.getCurrTime()
        return when {
            plannedMedicineEntity.plannedDate > currDate -> PlannedMedicineEntity.StatusOfTaking.WAITING
            plannedMedicineEntity.plannedDate < currDate -> PlannedMedicineEntity.StatusOfTaking.NOT_TAKEN
            else -> when {
                plannedMedicineEntity.plannedTime < currTime -> PlannedMedicineEntity.StatusOfTaking.NOT_TAKEN
                else -> PlannedMedicineEntity.StatusOfTaking.WAITING
            }
        }
    }
}
