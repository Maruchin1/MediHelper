package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.*

data class PlanDetails(
    val medicinePlanId: String,
    val profileColor: String,
    val medicineId: String,
    val medicineName: String,
    val medicineType: String,
    val medicineUnit: String,
    val planType: Plan.Type,
    val startDate: AppDate,
    val endDate: AppDate?,
    val intakeDays: IntakeDays?,
    val timeDoseList: List<TimeDose>
) {
    constructor(plan: Plan, medicine: Medicine, profile: Profile) : this(
        medicinePlanId = plan.entityId,
        profileColor = profile.color,
        medicineId = plan.medicineId,
        medicineName = medicine.name,
        medicineUnit = medicine.unit,
        medicineType = medicine.type,
        planType = plan.planType,
        startDate = plan.startDate,
        endDate = plan.endDate,
        intakeDays = plan.intakeDays,
        timeDoseList = plan.timeDoseList
    )
}