package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.Plan

data class PlanItem(
    val medicinePlanId: String,
    val medicineName: String,
    val medicineType: String?,
    val planType: Plan.Type,
    val startDate: AppDate,
    val endDate: AppDate?
) {
    constructor(plan: Plan, medicine: Medicine) : this(
        medicinePlanId = plan.entityId,
        medicineName = medicine.name,
        medicineType = medicine.type,
        planType = plan.planType,
        startDate = plan.startDate,
        endDate = plan.endDate
    )
}