package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineState

data class MedicineItem(
    val medicineId: String,
    val name: String,
    val unit: String,
    val state: MedicineState,
    val pictureName: String?
) {
    constructor(medicine: Medicine) : this(
        medicineId = medicine.entityId,
        name = medicine.name,
        unit = medicine.unit,
        state = medicine.state,
        pictureName = medicine.pictureName
    )
}