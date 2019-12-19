package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo

class DeleteMedicinePlanUseCase(
    private val medicinePlanRepo: MedicinePlanRepo
) {
    suspend fun execute(medicinePlanId: String) {
        medicinePlanRepo.deleteById(medicinePlanId)
    }
}