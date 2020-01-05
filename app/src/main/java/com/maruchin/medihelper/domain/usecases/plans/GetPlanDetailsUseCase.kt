package com.maruchin.medihelper.domain.usecases.plans

import com.maruchin.medihelper.domain.model.PlanDetails
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.MedicinePlanNotFoundException
import com.maruchin.medihelper.domain.usecases.ProfileNotFoundException

interface GetPlanDetailsUseCase {

    @Throws(
        MedicinePlanNotFoundException::class,
        MedicineNotFoundException::class,
        ProfileNotFoundException::class
    )
    suspend fun execute(medicinePlanId: String): PlanDetails
}