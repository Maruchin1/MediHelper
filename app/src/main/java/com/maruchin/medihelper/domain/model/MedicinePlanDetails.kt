package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.*

data class MedicinePlanDetails(
    val medicinePlanId: String,
    val profileColor: String,
    val medicineId: String,
    val medicineName: String,
    val medicineUnit: String,
    val planType: MedicinePlan.Type,
    val startDate: AppDate,
    val endDate: AppDate?,
    val intakeDays: IntakeDays?,
    val timeDoseList: List<TimeDose>
) {
    constructor(medicinePlan: MedicinePlan, medicine: Medicine, profile: Profile) : this(
        medicinePlanId = medicinePlan.entityId,
        profileColor = profile.color,
        medicineId = medicinePlan.medicineId,
        medicineName = medicine.name,
        medicineUnit = medicine.unit,
        planType = medicinePlan.planType,
        startDate = medicinePlan.startDate,
        endDate = medicinePlan.endDate,
        intakeDays = medicinePlan.intakeDays,
        timeDoseList = medicinePlan.timeDoseList
    )
}