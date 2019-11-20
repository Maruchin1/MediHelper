package com.example.medihelper.domain.entities

data class PlannedMedicine(
    val plannedMedicineId: Int,
    val medicinePlanId: Int,
    val plannedDate: AppDate,
    val plannedTime: AppTime,
    val plannedDoseSize: Float,
    val statusOfTaking: StatusOfTaking
)