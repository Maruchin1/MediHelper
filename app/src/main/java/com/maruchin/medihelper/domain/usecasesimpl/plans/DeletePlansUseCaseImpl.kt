package com.maruchin.medihelper.domain.usecasesimpl.plans

import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.usecases.plans.DeletePlansUseCase

class DeletePlansUseCaseImpl(
    private val planRepo: PlanRepo
) : DeletePlansUseCase {

    override suspend fun execute(medicinePlanIds: List<String>) {
        planRepo.deleteListById(medicinePlanIds)
    }
}