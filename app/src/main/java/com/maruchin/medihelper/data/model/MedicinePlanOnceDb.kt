package com.maruchin.medihelper.data.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.MedicinePlanOnce
import com.maruchin.medihelper.domain.entities.TimeDose

data class MedicinePlanOnceDb(
    val profileId: String? = null,
    val medicineId: String? = null,
    val date: String? = null,
    val timeDoseList: List<TimeDose>? = null
) {
    constructor(entity: MedicinePlanOnce) : this(
        profileId = entity.profileId,
        medicineId = entity.medicineId,
        date = entity.date.jsonFormatString,
        timeDoseList = entity.timeDoseList
    )

    fun toEntity(id: String) = MedicinePlanOnce(
        medicinePlanId = id,
        profileId = profileId!!,
        medicineId = medicineId!!,
        date = AppDate(date!!),
        timeDoseList = timeDoseList!!
    )
}