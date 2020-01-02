package com.maruchin.medihelper.domain.usecasesimpl.mediplans

import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.model.MedicinePlanEditData
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.usecases.MedicinePlanNotFoundException
import com.maruchin.medihelper.domain.usecases.mediplans.GetMedicinePlanEditDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMedicinePlanEditDataUseCaseImpl(
    private val medicinePlanRepo: MedicinePlanRepo
) :GetMedicinePlanEditDataUseCase {

    override suspend fun execute(medicinePlanId: String): MedicinePlanEditData = withContext(Dispatchers.Default) {
        val medicinePlan = getMedicinePlan(medicinePlanId)
        return@withContext MedicinePlanEditData(medicinePlan)
    }

    private suspend fun getMedicinePlan(medicinePlanId: String): MedicinePlan {
        return medicinePlanRepo.getById(medicinePlanId) ?: throw MedicinePlanNotFoundException()
    }
}