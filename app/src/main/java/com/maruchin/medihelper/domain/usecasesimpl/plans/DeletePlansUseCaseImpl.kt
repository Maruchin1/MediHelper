package com.maruchin.medihelper.domain.usecasesimpl.plans

import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.plans.DeletePlansUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.DeletePlannedMedicinesUseCase

class DeletePlansUseCaseImpl(
    private val planRepo: PlanRepo,
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val deletePlannedMedicinesUseCase: DeletePlannedMedicinesUseCase
) : DeletePlansUseCase {

    override suspend fun execute(medicinePlanIds: List<String>) {
        planRepo.deleteListById(medicinePlanIds)
        medicinePlanIds.forEach { planId ->
            deletePlannedMedicinesFromPlan(planId)
        }
    }

    private suspend fun deletePlannedMedicinesFromPlan(medicinePlanId: String) {
        val plannedMedicinesFromPlan = plannedMedicineRepo.getListByMedicinePlan(medicinePlanId)
        deletePlannedMedicinesUseCase.execute(plannedMedicinesFromPlan)
    }
}