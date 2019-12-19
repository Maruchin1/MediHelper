package com.maruchin.medihelper.domain.entities

data class MedicineCalendarEntry(
    val medicineCalendarEntryId: String,
    val medicinePlanId: String,
    val plannedDate: AppDate,
    val plannedTime: AppTime,
    val plannedDoseSize: Float,
    var status: Status
) {
    enum class Status {
        TAKEN, NOT_TAKEN, WAITING
    }
}