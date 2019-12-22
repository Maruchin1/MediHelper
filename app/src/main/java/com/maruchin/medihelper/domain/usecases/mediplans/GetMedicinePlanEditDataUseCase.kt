package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.model.MedicinePlanEditData
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMedicinePlanEditDataUseCase(
    private val medicinePlanRepo: MedicinePlanRepo
) {
    suspend fun execute(medicinePlanId: String): MedicinePlanEditData? = withContext(Dispatchers.Default) {
        val medicinePlan = medicinePlanRepo.getById(medicinePlanId)
        return@withContext medicinePlan?.let { MedicinePlanEditData(it) }
    }
}