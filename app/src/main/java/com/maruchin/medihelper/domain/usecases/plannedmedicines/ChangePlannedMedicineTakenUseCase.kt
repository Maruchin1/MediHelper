package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangePlannedMedicineTakenUseCase(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(plannedMedicineId: String) = withContext(Dispatchers.Default) {
        plannedMedicineRepo.getById(plannedMedicineId)?.let { plannedMedicine ->
            val newStatus = when (plannedMedicine.status) {
                PlannedMedicine.Status.NOT_TAKEN -> PlannedMedicine.Status.TAKEN
                PlannedMedicine.Status.TAKEN -> PlannedMedicine.Status.NOT_TAKEN
            }
            plannedMedicine.status = newStatus
            plannedMedicineRepo.update(plannedMedicine)

            changeMedicineState(plannedMedicine.medicineId, newStatus, plannedMedicine.plannedDoseSize)
        }
        return@withContext
    }

    private suspend fun changeMedicineState(medicineId: String, newStatus: PlannedMedicine.Status, doseSize: Float) {
        val medicine = medicineRepo.getById(medicineId)!!
        if (newStatus == PlannedMedicine.Status.TAKEN) {
            medicine.reduceCurrState(doseSize)
        } else {
            medicine.increaseCurrState(doseSize)
        }
        medicineRepo.update(medicine)
    }
}