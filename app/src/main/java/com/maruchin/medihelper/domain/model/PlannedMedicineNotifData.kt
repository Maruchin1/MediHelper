package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.*
import java.io.Serializable

data class PlannedMedicineNotifData(
    val plannedMedicineId: String,
    val profileName: String,
    val profileColor: String,
    val medicineName: String,
    val medicineUnit: String,
    val medicineType: String?,
    val plannedDate: AppDate,
    val plannedTime: AppTime,
    val doseSize: Float,
    val status: PlannedMedicine.Status
) : Serializable{
    constructor(plannedMedicine: PlannedMedicine, profile: Profile, medicine: Medicine) : this(
        plannedMedicineId = plannedMedicine.entityId,
        profileName = profile.name,
        profileColor = profile.color,
        medicineName = medicine.name,
        medicineUnit = medicine.unit,
        medicineType = medicine.type,
        plannedDate = plannedMedicine.plannedDate,
        plannedTime = plannedMedicine.plannedTime,
        doseSize = plannedMedicine.plannedDoseSize,
        status = plannedMedicine.status
    )
}