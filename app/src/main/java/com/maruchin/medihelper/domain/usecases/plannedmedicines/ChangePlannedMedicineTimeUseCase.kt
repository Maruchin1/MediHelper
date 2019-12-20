package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo

class ChangePlannedMedicineTimeUseCase(
    private val plannedMedicineRepo: PlannedMedicineRepo
) {
    suspend fun execute(plannedMedicineId: String, newPlannedTime: AppTime) {
        plannedMedicineRepo.getById(plannedMedicineId)?.let { plannedMedicine ->
            plannedMedicine.plannedTime = newPlannedTime
            plannedMedicineRepo.update(plannedMedicine)
        }
    }
}