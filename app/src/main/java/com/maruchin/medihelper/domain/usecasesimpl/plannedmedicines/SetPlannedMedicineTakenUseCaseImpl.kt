package com.maruchin.medihelper.domain.usecasesimpl.plannedmedicines

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.PlannedMedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.plannedmedicines.SetPlannedMedicineTakenUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SetPlannedMedicineTakenUseCaseImpl(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val medicineRepo: MedicineRepo
) : SetPlannedMedicineTakenUseCase {

    override suspend fun execute(plannedMedicineId: String) = withContext(Dispatchers.Default) {
        val plannedMedicine = getPlannedMedicine(plannedMedicineId)
        if (plannedMedicine.status == PlannedMedicine.Status.NOT_TAKEN) {
            changeStatusToTaken(plannedMedicine)
            reduceMedicineState(plannedMedicine.medicineId, plannedMedicine.plannedDoseSize)
        }
        return@withContext
    }

    private suspend fun getPlannedMedicine(plannedMedicineId: String): PlannedMedicine {
        return plannedMedicineRepo.getById(plannedMedicineId) ?: throw PlannedMedicineNotFoundException()
    }

    private suspend fun changeStatusToTaken(plannedMedicine: PlannedMedicine) {
        plannedMedicine.status = PlannedMedicine.Status.TAKEN
        plannedMedicineRepo.update(plannedMedicine)
    }

    private suspend fun reduceMedicineState(medicineId: String, doseSize: Float) {
        val medicine = getMedicine(medicineId)
        medicine.state.reduce(doseSize)
        medicineRepo.update(medicine)
    }

    private suspend fun getMedicine(medicineId: String): Medicine {
        return medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
    }
}