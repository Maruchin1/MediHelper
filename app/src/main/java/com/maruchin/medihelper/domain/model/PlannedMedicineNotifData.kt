package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.entities.Profile
import java.io.Serializable

data class PlannedMedicineNotifData(
    val plannedMedicineId: String,
    val profileName: String,
    val profileColor: String,
    val medicineName: String,
    val medicineUnit: String,
    val medicineType: String?,
    val plannedTime: String,
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
        plannedTime = plannedMedicine.plannedTime.formatString,
        doseSize = plannedMedicine.plannedDoseSize,
        status = plannedMedicine.status
    )
}