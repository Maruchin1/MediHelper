package com.maruchin.medihelper.domain.usecasesimpl.plans

import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.plans.DeleteSinglePlanUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.DeletePlannedMedicinesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteSinglePlanUseCaseImpl(
    private val planRepo: PlanRepo,
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val deletePlannedMedicinesUseCase: DeletePlannedMedicinesUseCase
) : DeleteSinglePlanUseCase {

    override suspend fun execute(medicinePlanId: String) = withContext(Dispatchers.Default) {
        val plannedMedicines = plannedMedicineRepo.getListByMedicinePlan(medicinePlanId)
        planRepo.deleteById(medicinePlanId)
        deletePlannedMedicinesUseCase.execute(plannedMedicines)
        return@withContext
    }
}