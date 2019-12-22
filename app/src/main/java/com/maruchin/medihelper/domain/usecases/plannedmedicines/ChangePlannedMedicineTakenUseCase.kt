package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo

class ChangePlannedMedicineTakenUseCase(
    private val plannedMedicineRepo: PlannedMedicineRepo
) {
    suspend fun execute(plannedMedicineId: String) {
        plannedMedicineRepo.getById(plannedMedicineId)?.let { plannedMedicine ->
            val newStatus = when (plannedMedicine.status) {
                PlannedMedicine.Status.NOT_TAKEN -> PlannedMedicine.Status.TAKEN
                PlannedMedicine.Status.TAKEN -> PlannedMedicine.Status.NOT_TAKEN
            }
            plannedMedicine.status = newStatus
            plannedMedicineRepo.update(plannedMedicine)
        }
    }
}