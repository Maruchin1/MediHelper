package com.maruchin.medihelper.domain.usecasesimpl.mediplans

import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.mediplans.DeleteMedicinesPlansUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.DeletePlannedMedicinesUseCase

class DeleteMedicinesPlansUseCaseImpl(
    private val medicinePlanRepo: MedicinePlanRepo,
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val deletePlannedMedicinesUseCase: DeletePlannedMedicinesUseCase
) : DeleteMedicinesPlansUseCase {

    override suspend fun execute(medicinePlanIds: List<String>) {
        medicinePlanRepo.deleteListById(medicinePlanIds)
        medicinePlanIds.forEach { planId ->
            deletePlannedMedicinesFromPlan(planId)
        }
    }

    private suspend fun deletePlannedMedicinesFromPlan(medicinePlanId: String) {
        val plannedMedicinesFromPlan = plannedMedicineRepo.getListByMedicinePlan(medicinePlanId)
        deletePlannedMedicinesUseCase.execute(plannedMedicinesFromPlan)
    }
}