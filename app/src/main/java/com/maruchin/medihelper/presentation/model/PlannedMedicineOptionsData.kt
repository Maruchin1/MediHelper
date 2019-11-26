package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicineWithMedicine
import com.maruchin.medihelper.domain.entities.StatusOfTaking

class PlannedMedicineOptionsData(
    val medicineName: String,
    val medicineUnit: String,
    val plannedDate: AppDate,
    val plannedTime: AppTime,
    val plannedDoseSize: Float,
    val statusOfTaking: StatusOfTaking
) {
    constructor(plannedMedicineWithMedicine: PlannedMedicineWithMedicine) : this(
        medicineName = plannedMedicineWithMedicine.medicine.name,
        medicineUnit = plannedMedicineWithMedicine.medicine.unit,
        plannedDate = plannedMedicineWithMedicine.plannedDate,
        plannedTime = plannedMedicineWithMedicine.plannedTime,
        plannedDoseSize = plannedMedicineWithMedicine.plannedDoseSize,
        statusOfTaking = plannedMedicineWithMedicine.statusOfTaking
    )
}