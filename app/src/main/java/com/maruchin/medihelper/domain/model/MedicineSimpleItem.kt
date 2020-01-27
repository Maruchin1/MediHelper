package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.Medicine

data class MedicineSimpleItem(
    val medicineId: String,
    val name: String,
    val type: String,
    val unit: String
) {
    constructor(medicine: Medicine) : this(
        medicineId = medicine.entityId,
        name = medicine.name,
        type = medicine.type,
        unit = medicine.unit
    )
}