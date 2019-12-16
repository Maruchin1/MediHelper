package com.maruchin.medihelper.domain.entities

data class MedicinePlanContinuous(
    override val medicinePlanId: String,
    override val medicineId: String,
    override val profileId: String,
    override val timeDoseList: List<TimeDose>,
    val startDate: AppDate,
    val intakeDays: IntakeDays
) : MedicinePlan()