package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineStateData

data class MedicineItem(
    val medicineId: String,
    val name: String,
    val unit: String,
    val stateData: MedicineStateData
) {
    constructor(medicine: Medicine) : this(
        medicineId = medicine.medicineId,
        name = medicine.name,
        unit = medicine.unit,
        stateData = medicine.getStateData()
    )
}