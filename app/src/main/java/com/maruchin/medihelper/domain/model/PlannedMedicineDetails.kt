package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.PlannedMedicine

data class PlannedMedicineDetails(
    val plannedMedicineId: String,
    val medicinePlanId: String,
    val medicineName: String,
    val medicineUnit: String,
    val plannedDate: AppDate,
    val plannedTime: AppTime,
    val plannedDoseSize: Float,
    val status: PlannedMedicine.Status
) {
    constructor(plannedMedicine: PlannedMedicine, medicine: Medicine) : this(
        plannedMedicineId = plannedMedicine.entityId,
        medicinePlanId = plannedMedicine.medicinePlanId,
        medicineName = medicine.name,
        medicineUnit = medicine.unit,
        plannedDate = plannedMedicine.plannedDate,
        plannedDoseSize = plannedMedicine.plannedDoseSize,
        plannedTime = plannedMedicine.plannedTime,
        status = plannedMedicine.status
    )
}