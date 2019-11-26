package com.maruchin.medihelper.domain.entities

data class PlannedMedicineWithMedicine(
    val plannedMedicineId: Int,
    val medicine: Medicine,
    val plannedDate: AppDate,
    val plannedTime: AppTime,
    val plannedDoseSize: Float,
    val statusOfTaking: StatusOfTaking
)