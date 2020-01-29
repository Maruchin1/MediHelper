package com.maruchin.medihelper.domain.usecasesimpl.plannedmedicines

import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.PlannedMedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.plannedmedicines.ChangePlannedMedicineTimeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangePlannedMedicineTimeUseCaseImpl(
    private val plannedMedicineRepo: PlannedMedicineRepo
) : ChangePlannedMedicineTimeUseCase {

    override suspend fun execute(plannedMedicineId: String, newPlannedTime: AppTime) = withContext(Dispatchers.Default) {
        val plannedMedicine = getPlannedMedicine(plannedMedicineId)
        updatePlannedMedicineTime(plannedMedicine, newPlannedTime)
        return@withContext
    }

    private suspend fun getPlannedMedicine(plannedMedicineId: String): PlannedMedicine {
        return plannedMedicineRepo.getById(plannedMedicineId) ?: throw PlannedMedicineNotFoundException()
    }

    private suspend fun updatePlannedMedicineTime(plannedMedicine: PlannedMedicine, newPlannedTime: AppTime) {
        plannedMedicine.plannedTime = newPlannedTime
        plannedMedicineRepo.update(plannedMedicine)
    }
}