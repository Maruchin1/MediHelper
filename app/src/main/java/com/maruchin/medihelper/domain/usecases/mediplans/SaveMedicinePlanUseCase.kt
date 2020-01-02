package com.maruchin.medihelper.domain.usecases.mediplans

import com.maruchin.medihelper.domain.entities.*
import com.maruchin.medihelper.domain.model.MedicinePlanErrors

interface SaveMedicinePlanUseCase {

    suspend fun execute(params: Params): MedicinePlanErrors

    data class Params(
        val medicinePlanId: String?,
        val profileId: String?,
        val medicineId: String?,
        val planType: MedicinePlan.Type?,
        val startDate: AppDate?,
        val endDate: AppDate?,
        val intakeDays: IntakeDays?,
        val timeDoseList: List<TimeDose>?
    )
}