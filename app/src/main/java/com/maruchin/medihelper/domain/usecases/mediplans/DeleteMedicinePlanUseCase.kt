package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo

class DeleteMedicinePlanUseCase(
    private val medicinePlanRepo: MedicinePlanRepo,
    private val plannedMedicineRepo: PlannedMedicineRepo
) {
    suspend fun execute(medicinePlanId: String) {
        medicinePlanRepo.deleteById(medicinePlanId)
        val plannedMedicines = plannedMedicineRepo.getListByMedicinePlan(medicinePlanId)
        plannedMedicines.forEach {
            plannedMedicineRepo.deleteById(it.plannedMedicineId)
        }
    }
}