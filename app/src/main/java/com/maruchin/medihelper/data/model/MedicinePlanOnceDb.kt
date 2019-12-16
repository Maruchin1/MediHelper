package com.maruchin.medihelper.data.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.MedicinePlanOnce
import com.maruchin.medihelper.domain.entities.TimeDose

data class MedicinePlanOnceDb(
    val planType: String = "ONCE",
    val profileId: String? = null,
    val medicineId: String? = null,
    val date: String? = null,
    val timeDoseList: List<TimeDoseDb>? = null
) {
    constructor(entity: MedicinePlanOnce) : this(
        profileId = entity.profileId,
        medicineId = entity.medicineId,
        date = entity.date.jsonFormatString,
        timeDoseList = entity.timeDoseList.map { TimeDoseDb(it) }
    )

    fun toEntity(id: String) = MedicinePlanOnce(
        medicinePlanId = id,
        profileId = profileId!!,
        medicineId = medicineId!!,
        date = AppDate(date!!),
        timeDoseList = timeDoseList!!.map { it.toEntity() }
    )
}