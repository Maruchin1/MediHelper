package com.maruchin.medihelper.domain.entities

data class MedicinePlanInputData(
    val medicineId: Int,
    val personId: Int,
    val durationType: DurationType,
    val startDate: AppDate,
    val endDate: AppDate?,
    val daysType: DaysType?,
    val daysOfWeek: DaysOfWeek?,
    val intervalOfDays: Int?,
    val timeDoseList: List<TimeDose>
)