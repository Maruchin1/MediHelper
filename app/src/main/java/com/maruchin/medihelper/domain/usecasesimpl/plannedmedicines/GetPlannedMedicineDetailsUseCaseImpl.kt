package com.maruchin.medihelper.domain.usecasesimpl.plannedmedicines

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.PlannedMedicineDetails
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.PlannedMedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetPlannedMedicineDetailsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPlannedMedicineDetailsUseCaseImpl(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val medicineRepo: MedicineRepo
) : GetPlannedMedicineDetailsUseCase {

    override suspend fun execute(plannedMedicineId: String): PlannedMedicineDetails = withContext(Dispatchers.Default) {
        val plannedMedicine = getPlannedMedicine(plannedMedicineId)
        val medicine = getMedicine(plannedMedicine.medicineId)
        return@withContext PlannedMedicineDetails(plannedMedicine, medicine)
    }

    private suspend fun getPlannedMedicine(plannedMedicineId: String): PlannedMedicine {
        return plannedMedicineRepo.getById(plannedMedicineId) ?: throw PlannedMedicineNotFoundException()
    }

    private suspend fun getMedicine(medicineId: String): Medicine {
        return medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
    }
}