package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.PlannedMedicine

data class PlannedMedicineItem(
    val plannedMedicineId: String,
    val medicineName: String,
    val medicineUnit: String,
    val plannedDoseSize: Float,
    val plannedTime: AppTime,
    val status: PlannedMedicine.Status
) {
    constructor(plannedMedicine: PlannedMedicine, medicine: Medicine) : this(
        plannedMedicineId = plannedMedicine.entityId,
        medicineName = medicine.name,
        medicineUnit = medicine.unit,
        plannedDoseSize = plannedMedicine.plannedDoseSize,
        plannedTime = plannedMedicine.plannedTime,
        status = plannedMedicine.status
    )
}