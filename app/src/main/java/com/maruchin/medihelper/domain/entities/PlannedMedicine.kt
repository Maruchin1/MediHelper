package com.maruchin.medihelper.domain.entities

data class PlannedMedicine(
    val medicinePlanId: String,
    val profileId: String,
    val medicineId: String,
    val plannedDate: AppDate,
    var plannedTime: AppTime,
    val plannedDoseSize: Float,
    var status: Status
) {

    enum class Status {
        TAKEN, NOT_TAKEN
    }
}