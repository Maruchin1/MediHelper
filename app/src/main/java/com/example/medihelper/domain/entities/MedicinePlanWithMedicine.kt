package com.example.medihelper.domain.entities

data class MedicinePlanWithMedicine(
    val medicinePlanId: Int,
    val medicine: Medicine,
    val personId: Int,
    val durationType: DurationType,
    val startDate: AppDate,
    val endDate: AppDate?,
    val daysType: DaysType?,
    val daysOfWeek: DaysOfWeek?,
    val intervalOfDays: Int?,
    val timeDoseList: List<TimeDose>
) {
    fun getType(currDate: AppDate) = MedicinePlanType.getType(durationType, startDate, endDate, currDate)
}