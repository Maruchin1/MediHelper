package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.IntakeDays
import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.entities.TimeDose

data class PlanEditData(
    val medicinePlanId: String,
    val profileId: String,
    val medicineId: String,
    val planType: Plan.Type,
    val startDate: AppDate,
    val endDate: AppDate?,
    val intakeDays: IntakeDays?,
    val timeDoseList: List<TimeDose>
) {
    constructor(entity: Plan) : this(
        medicinePlanId = entity.entityId,
        profileId = entity.profileId,
        medicineId = entity.medicineId,
        planType = entity.planType,
        startDate = entity.startDate,
        endDate = entity.endDate,
        intakeDays = entity.intakeDays,
        timeDoseList = entity.timeDoseList
    )
}