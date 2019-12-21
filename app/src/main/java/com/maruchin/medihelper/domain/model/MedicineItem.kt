package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineStateData

data class MedicineItem(
    val medicineId: String,
    val name: String,
    val unit: String,
    val stateData: MedicineStateData?,
    val pictureName: String?
) {
    constructor(medicine: Medicine) : this(
        medicineId = medicine.entityId,
        name = medicine.name,
        unit = medicine.unit,
        stateData = medicine.stateData,
        pictureName = medicine.pictureName
    )
}