package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo

class ChangePlannedMedicineTakenUseCase(
    private val plannedMedicineRepo: PlannedMedicineRepo
) {
    suspend fun execute(plannedMedicineId: String) {
        plannedMedicineRepo.getById(plannedMedicineId)?.let { plannedMedicine ->
            val newTaken = !plannedMedicine.taken
            plannedMedicine.taken = newTaken
            plannedMedicineRepo.update(plannedMedicine)
        }
    }
}