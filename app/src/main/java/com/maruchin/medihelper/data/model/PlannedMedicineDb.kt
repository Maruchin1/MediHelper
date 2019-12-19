package com.maruchin.medihelper.data.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicine

data class PlannedMedicineDb(
    val medicinePlanId: String? = null,
    val medicineId: String? = null,
    val plannedDate: String? = null,
    val plannedTime: String? = null,
    val plannedDoseSize: Float? = null,
    val status: PlannedMedicine.Status? = null
) {
    constructor(entity: PlannedMedicine) : this(
        medicinePlanId = entity.medicinePlanId,
        medicineId = entity.medicineId,
        plannedDate = entity.plannedDate.jsonFormatString,
        plannedTime = entity.plannedTime.jsonFormatString,
        plannedDoseSize = entity.plannedDoseSize,
        status = entity.status
    )

    fun toEntity(id: String) = PlannedMedicine(
        plannedMedicineId = id,
        medicinePlanId = medicinePlanId!!,
        medicineId = medicineId!!,
        plannedDate = AppDate(plannedDate!!),
        plannedTime = AppTime(plannedTime!!),
        plannedDoseSize = plannedDoseSize!!,
        status = status!!
    )
}