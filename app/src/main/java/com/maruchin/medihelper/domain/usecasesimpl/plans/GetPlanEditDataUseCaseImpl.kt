package com.maruchin.medihelper.domain.usecasesimpl.plans

import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.model.PlanEditData
import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.usecases.MedicinePlanNotFoundException
import com.maruchin.medihelper.domain.usecases.plans.GetPlanEditDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPlanEditDataUseCaseImpl(
    private val planRepo: PlanRepo
) :GetPlanEditDataUseCase {

    override suspend fun execute(medicinePlanId: String): PlanEditData = withContext(Dispatchers.Default) {
        val medicinePlan = getMedicinePlan(medicinePlanId)
        return@withContext PlanEditData(medicinePlan)
    }

    private suspend fun getMedicinePlan(medicinePlanId: String): Plan {
        return planRepo.getById(medicinePlanId) ?: throw MedicinePlanNotFoundException()
    }
}