package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.model.PlannedMedicineDetails
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo

class GetPlannedMedicineDetailsUseCase(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(plannedMedicineId: String): PlannedMedicineDetails? {
        return plannedMedicineRepo.getById(plannedMedicineId)?.let { plannedMedicine ->
            val medicine = medicineRepo.getById(plannedMedicine.medicineId) ?: throw Exception("Medicine doesn't exist")
            PlannedMedicineDetails(plannedMedicine, medicine)
        }
    }
}