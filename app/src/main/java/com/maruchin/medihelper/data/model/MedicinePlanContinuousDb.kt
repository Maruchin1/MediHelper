package com.maruchin.medihelper.data.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.IntakeDays
import com.maruchin.medihelper.domain.entities.MedicinePlanContinuous

data class MedicinePlanContinuousDb(
    val planType: String = "CONTINUOUS",
    val profileId: String? = null,
    val medicineId: String? = null,
    val startDate: String? = null,
    val intakeDays: IntakeDays? = null,
    val timeDoseList: List<TimeDoseDb>? = null
) {
    constructor(entity: MedicinePlanContinuous) : this(
        profileId = entity.profileId,
        medicineId = entity.medicineId,
        startDate = entity.startDate.jsonFormatString,
        intakeDays = entity.intakeDays,
        timeDoseList = entity.timeDoseList.map { TimeDoseDb(it) }
    )

    fun toEntity(id: String) = MedicinePlanContinuous(
        medicinePlanId = id,
        profileId = profileId!!,
        medicineId = medicineId!!,
        startDate = AppDate(startDate!!),
        intakeDays = intakeDays!!,
        timeDoseList = timeDoseList!!.map { it.toEntity() }
    )
}