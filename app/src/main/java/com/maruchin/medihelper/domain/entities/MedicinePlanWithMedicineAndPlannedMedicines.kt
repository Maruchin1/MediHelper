package com.maruchin.medihelper.domain.entities

data class MedicinePlanWithMedicineAndPlannedMedicines(
    val medicinePlanWithMedicine: MedicinePlanWithMedicine,
    val plannedMedicineList: List<PlannedMedicine>
)