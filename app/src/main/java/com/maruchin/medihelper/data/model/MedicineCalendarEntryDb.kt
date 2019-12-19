package com.maruchin.medihelper.data.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.MedicineCalendarEntry

data class MedicineCalendarEntryDb(
    val plannedDate: String? = null,
    val plannedTime: String? = null,
    val plannedDoseSize: Float? = null,
    val status: MedicineCalendarEntry.Status? = null
) {
    constructor(entity: MedicineCalendarEntry) : this(
        plannedDate = entity.plannedDate.jsonFormatString,
        plannedTime = entity.plannedTime.jsonFormatString,
        plannedDoseSize = entity.plannedDoseSize,
        status = entity.status
    )

    fun toEntity(id: String, medicinePlanId: String) = MedicineCalendarEntry(
        medicineCalendarEntryId = id,
        medicinePlanId = medicinePlanId,
        plannedDate = AppDate(plannedDate!!),
        plannedTime = AppTime(plannedTime!!),
        plannedDoseSize = plannedDoseSize!!,
        status = status!!
    )
}