package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicinePlan

data class MedicinePlanItem(
    val medicinePlanId: String,
    val medicineName: String,
    val medicineUnit: String,
    val planType: MedicinePlan.Type,
    val startDate: AppDate,
    val endDate: AppDate?
) {
    constructor(medicinePlan: MedicinePlan, medicine: Medicine) : this(
        medicinePlanId = medicinePlan.medicinePlanId,
        medicineName = medicine.name,
        medicineUnit = medicine.unit,
        planType = medicinePlan.planType,
        startDate = medicinePlan.startDate,
        endDate = medicinePlan.endDate
    )
}