package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangePlannedMedicineTimeUseCase(
    private val plannedMedicineRepo: PlannedMedicineRepo
) {
    suspend fun execute(plannedMedicineId: String, newPlannedTime: AppTime) = withContext(Dispatchers.Default) {
        plannedMedicineRepo.getById(plannedMedicineId)?.let { plannedMedicine ->
            plannedMedicine.plannedTime = newPlannedTime
            plannedMedicineRepo.update(plannedMedicine)
        }
        return@withContext
    }
}