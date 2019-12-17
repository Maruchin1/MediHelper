package com.maruchin.medihelper.domain.entities

data class MedicinePlan(
    val medicinePlanId: String,
    val profileId: String,
    val medicineId: String,
    val planType: Type,
    val startDate: AppDate,
    val endDate: AppDate?,
    val intakeDays: IntakeDays?,
    val timeDoseList: List<TimeDose>
) {
    enum class Type {
        ONCE, PERIOD, CONTINUOUS
    }
}