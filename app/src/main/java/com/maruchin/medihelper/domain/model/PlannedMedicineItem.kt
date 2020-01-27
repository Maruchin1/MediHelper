package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.PlannedMedicine

data class PlannedMedicineItem(
    val medicinePlanId: String,
    val medicineName: String,
    val medicineUnit: String,
    val medicineType: String?,
    val plannedDate: AppDate,
    val plannedTime: AppTime,
    val plannedDoseSize: Float,
    val status: PlannedMedicine.Status
) {
    constructor(plannedMedicine: PlannedMedicine, medicine: Medicine) : this(
        medicinePlanId = plannedMedicine.medicinePlanId,
        medicineName = medicine.name,
        medicineUnit = medicine.unit,
        medicineType = medicine.type,
        plannedDate = plannedMedicine.plannedDate,
        plannedTime = plannedMedicine.plannedTime,
        plannedDoseSize = plannedMedicine.plannedDoseSize,
        status = plannedMedicine.status
    )
}