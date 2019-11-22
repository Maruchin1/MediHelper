package com.example.medihelper.presentation.model

import com.example.medihelper.domain.entities.AppTime
import com.example.medihelper.domain.entities.PlannedMedicineWithMedicine
import com.example.medihelper.domain.entities.StatusOfTaking

data class PlannedMedicineItem(
    val plannedMedicineId: Int,
    val medicineName: String,
    val medicineUnit: String,
    val plannedDoseSize: Float,
    val plannedTime: AppTime,
    val statusOfTaking: StatusOfTaking
) {
    constructor(plannedMedicineWithMedicine: PlannedMedicineWithMedicine) : this(
        plannedMedicineId = plannedMedicineWithMedicine.plannedMedicineId,
        medicineName = plannedMedicineWithMedicine.medicine.name,
        medicineUnit = plannedMedicineWithMedicine.medicine.unit,
        plannedDoseSize = plannedMedicineWithMedicine.plannedDoseSize,
        plannedTime = plannedMedicineWithMedicine.plannedTime,
        statusOfTaking = plannedMedicineWithMedicine.statusOfTaking
    )
}