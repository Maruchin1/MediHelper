package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.IntakeDays
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.entities.TimeDose

data class MedicinePlanEditData(
    val medicinePlanId: String,
    val profileId: String,
    val medicineId: String,
    val planType: MedicinePlan.Type,
    val startDate: AppDate,
    val endDate: AppDate?,
    val intakeDays: IntakeDays?,
    val timeDoseList: List<TimeDose>
) {
    constructor(entity: MedicinePlan) : this(
        medicinePlanId = entity.medicinePlanId,
        profileId = entity.profileId,
        medicineId = entity.medicineId,
        planType = entity.planType,
        startDate = entity.startDate,
        endDate = entity.endDate,
        intakeDays = entity.intakeDays,
        timeDoseList = entity.timeDoseList
    )
}