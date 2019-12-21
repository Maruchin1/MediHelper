package com.maruchin.medihelper.data.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.MedicinePlan

data class MedicinePlanDb(
    val profileId: String? = null,
    val medicineId: String? = null,
    val planType: MedicinePlan.Type? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val intakeDays: IntakeDaysDb? = null,
    val timeDoseList: List<TimeDoseDb>? = null
) {
    constructor(entity: MedicinePlan) : this(
        profileId = entity.profileId,
        medicineId = entity.medicineId,
        planType = entity.planType,
        startDate = entity.startDate.jsonFormatString,
        endDate = entity.endDate?.jsonFormatString,
        intakeDays = entity.intakeDays?.let { IntakeDaysDb(it) },
        timeDoseList = entity.timeDoseList.map { TimeDoseDb(it) }
    )

    fun toEntity(id: String) = MedicinePlan(
        entityId = id,
        profileId = profileId!!,
        medicineId = medicineId!!,
        planType = planType!!,
        startDate = AppDate(startDate!!),
        endDate = endDate?.let { AppDate(it) },
        intakeDays = intakeDays?.toEntity(),
        timeDoseList = timeDoseList!!.map { it.toEntity() }
    )
}