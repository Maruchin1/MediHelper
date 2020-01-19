package com.maruchin.medihelper.domain.usecasesimpl.plans

import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.usecases.plans.DeleteSinglePlanUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteSinglePlanUseCaseImpl(
    private val planRepo: PlanRepo
) : DeleteSinglePlanUseCase {

    override suspend fun execute(medicinePlanId: String) = withContext(Dispatchers.Default) {
        planRepo.deleteById(medicinePlanId)
        return@withContext
    }
}