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
    fun getPlanType(currDate: AppDate) = when(durationType) {
        DurationType.ONCE -> {
            when {
                currDate > startDate -> MedicinePlanType.ENDED
                else -> MedicinePlanType.ONGOING
            }
        }
        DurationType.PERIOD -> {
            when {
                currDate > endDate!! -> MedicinePlanType.ENDED
                else -> MedicinePlanType.ONGOING
            }
        }
        DurationType.CONTINUOUS -> MedicinePlanType.ONGOING
    }
}