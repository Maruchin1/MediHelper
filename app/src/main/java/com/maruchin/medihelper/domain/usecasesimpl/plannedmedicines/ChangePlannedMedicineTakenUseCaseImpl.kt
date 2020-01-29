package com.maruchin.medihelper.domain.usecasesimpl.plannedmedicines

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.PlannedMedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.plannedmedicines.ChangePlannedMedicineTakenUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangePlannedMedicineTakenUseCaseImpl(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val medicineRepo: MedicineRepo
) : ChangePlannedMedicineTakenUseCase {

    override suspend fun execute(plannedMedicineId: String) = withContext(Dispatchers.Default) {
        val plannedMedicine = getPlannedMedicine(plannedMedicineId)
        val newStatus = getNewPlannedMedicineStatus(currStatus = plannedMedicine.status)
        updatePlannedMedicineStatus(plannedMedicine, newStatus)
        changeMedicineState(plannedMedicine.medicineId, newStatus, plannedMedicine.plannedDoseSize)
        return@withContext
    }

    private suspend fun getPlannedMedicine(plannedMedicineId: String): PlannedMedicine {
        return plannedMedicineRepo.getById(plannedMedicineId) ?: throw PlannedMedicineNotFoundException()
    }

    private fun getNewPlannedMedicineStatus(currStatus: PlannedMedicine.Status): PlannedMedicine.Status {
        return when (currStatus) {
            PlannedMedicine.Status.TAKEN -> PlannedMedicine.Status.NOT_TAKEN
            PlannedMedicine.Status.NOT_TAKEN -> PlannedMedicine.Status.TAKEN
        }
    }

    private suspend fun updatePlannedMedicineStatus(plannedMedicine: PlannedMedicine, newStatus: PlannedMedicine.Status) {
        plannedMedicine.status = newStatus
        plannedMedicineRepo.update(plannedMedicine)
    }

    private suspend fun changeMedicineState(medicineId: String, newStatus: PlannedMedicine.Status, doseSize: Float) {
        val medicine = getMedicine(medicineId)
        if (newStatus == PlannedMedicine.Status.TAKEN) {
            medicine.state.reduce(doseSize)
        } else {
            medicine.state.increase(doseSize)
        }
        medicineRepo.update(medicine)
    }

    private suspend fun getMedicine(medicineId: String): Medicine {
        return medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
    }
}