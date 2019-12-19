package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.model.MedicinePlanEditData
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo

class GetMedicinePlanEditDataUseCase(
    private val medicinePlanRepo: MedicinePlanRepo
) {
    suspend fun execute(medicinePlanId: String): MedicinePlanEditData? {
        val medicinePlan = medicinePlanRepo.getById(medicinePlanId)
        return medicinePlan?.let { MedicinePlanEditData(it) }
    }
}