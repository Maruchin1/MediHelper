package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.entities.Profile

data class PlannedMedicineNotfiData(
    val plannedMedicineId: String,
    val profileName: String,
    val profileColor: String,
    val medicineName: String,
    val plannedTime: AppTime
) {
    constructor(plannedMedicine: PlannedMedicine, profile: Profile, medicine: Medicine) : this(
        plannedMedicineId = plannedMedicine.entityId,
        profileName = profile.name,
        profileColor = profile.color,
        medicineName = medicine.name,
        plannedTime = plannedMedicine.plannedTime
    )
}