package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SetPlannedMedicineTakenUseCase(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(plannedMedicineId: String) = withContext(Dispatchers.Default) {
        plannedMedicineRepo.getById(plannedMedicineId)?.let { plannedMedicine ->
            if (plannedMedicine.status == PlannedMedicine.Status.NOT_TAKEN) {
                plannedMedicine.status = PlannedMedicine.Status.TAKEN

                plannedMedicineRepo.update(plannedMedicine)
                reduceMedicineState(plannedMedicine.medicineId, plannedMedicine.plannedDoseSize)
            }
        }
        return@withContext
    }

    private suspend fun reduceMedicineState(medicineId: String, doseSize: Float) {
        val medicine = medicineRepo.getById(medicineId)!!

        medicine.reduceCurrState(doseSize)
        medicineRepo.update(medicine)
    }
}