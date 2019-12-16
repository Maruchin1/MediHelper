package com.maruchin.medihelper.data.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.IntakeDays
import com.maruchin.medihelper.domain.entities.MedicinePlanPeriod

data class MedicinePlanPeriodDb(
    val planType: String = "PERIOD",
    val profileId: String? = null,
    val medicineId: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val intakeDays: IntakeDays? = null,
    val timeDoseList: List<TimeDoseDb>? = null
) {
    constructor(entity: MedicinePlanPeriod) : this(
        profileId = entity.profileId,
        medicineId = entity.medicineId,
        startDate = entity.startDate.jsonFormatString,
        endDate = entity.endDate.jsonFormatString,
        intakeDays = entity.intakeDays,
        timeDoseList = entity.timeDoseList.map { TimeDoseDb(it) }
    )

    fun toEntity(id: String) = MedicinePlanPeriod(
        medicinePlanId = id,
        profileId = profileId!!,
        medicineId = medicineId!!,
        startDate = AppDate(startDate!!),
        endDate = AppDate(endDate!!),
        intakeDays = intakeDays!!,
        timeDoseList = timeDoseList!!.map { it.toEntity() }
    )
}