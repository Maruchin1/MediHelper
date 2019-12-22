package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.model.PlannedMedicineDetails
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPlannedMedicineDetailsUseCase(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(plannedMedicineId: String): PlannedMedicineDetails? = withContext(Dispatchers.Default) {
        return@withContext plannedMedicineRepo.getById(plannedMedicineId)?.let { plannedMedicine ->
            val medicine = medicineRepo.getById(plannedMedicine.medicineId) ?: throw Exception("Medicine doesn't exist")
            PlannedMedicineDetails(plannedMedicine, medicine)
        }
    }
}