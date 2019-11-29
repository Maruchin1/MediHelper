package com.maruchin.medihelper.domain.entities

data class PlannedMedicine(
    val plannedMedicineId: Int,
    val medicinePlanId: Int,
    val plannedDate: AppDate,
    var plannedTime: AppTime,
    val plannedDoseSize: Float,
    var statusOfTaking: StatusOfTaking
)