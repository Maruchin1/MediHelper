package com.maruchin.medihelper.domain.usecases.plans

import com.maruchin.medihelper.domain.entities.*
import com.maruchin.medihelper.domain.model.PlanErrors

interface SavePlanUseCase {

    suspend fun execute(params: Params): PlanErrors

    data class Params(
        val medicinePlanId: String?,
        val profileId: String?,
        val medicineId: String?,
        val planType: Plan.Type?,
        val startDate: AppDate?,
        val endDate: AppDate?,
        val intakeDays: IntakeDays?,
        val timeDoseList: List<TimeDose>?
    )
}