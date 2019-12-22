package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteMedicinePlanUseCase(
    private val medicinePlanRepo: MedicinePlanRepo,
    private val plannedMedicineRepo: PlannedMedicineRepo
) {
    suspend fun execute(medicinePlanId: String) = withContext(Dispatchers.Default) {
        medicinePlanRepo.deleteById(medicinePlanId)
        val plannedMedicines = plannedMedicineRepo.getListByMedicinePlan(medicinePlanId)
        plannedMedicines.forEach {
            plannedMedicineRepo.deleteById(it.entityId)
        }
        return@withContext
    }
}