package com.example.medihelper.domain.entities

data class MedicinePlanWithMedicineAndPlannedMedicines(
    val medicinePlanWithMedicine: MedicinePlanWithMedicine,
    val plannedMedicineList: List<PlannedMedicine>
)