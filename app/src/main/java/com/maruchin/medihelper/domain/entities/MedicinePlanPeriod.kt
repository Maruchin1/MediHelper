package com.maruchin.medihelper.domain.entities

data class MedicinePlanPeriod(
    override val medicinePlanId: String,
    override val medicineId: String,
    override val profileId: String,
    override val timeDoseList: List<TimeDose>,
    val startDate: AppDate,
    val endDate: AppDate,
    val intakeDays: IntakeDays
) : MedicinePlan()