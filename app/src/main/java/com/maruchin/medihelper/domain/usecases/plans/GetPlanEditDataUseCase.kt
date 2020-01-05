package com.maruchin.medihelper.domain.usecases.plans

import com.maruchin.medihelper.domain.model.PlanEditData
import com.maruchin.medihelper.domain.usecases.MedicinePlanNotFoundException

interface GetPlanEditDataUseCase {

    @Throws(MedicinePlanNotFoundException::class)
    suspend fun execute(medicinePlanId: String): PlanEditData
}