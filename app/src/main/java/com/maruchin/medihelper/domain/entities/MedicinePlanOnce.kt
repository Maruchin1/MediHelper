package com.maruchin.medihelper.domain.entities

data class MedicinePlanOnce(
    override val medicinePlanId: String,
    override val medicineId: String,
    override val profileId: String,
    override val timeDoseList: List<TimeDose>,
    val date: AppDate
) : MedicinePlan()