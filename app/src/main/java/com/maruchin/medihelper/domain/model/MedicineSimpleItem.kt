package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.Medicine

data class MedicineSimpleItem(
    val medicineId: String,
    val name: String,
    val unit: String,
    val type: String?
) {
    constructor(medicine: Medicine) : this(
        medicineId = medicine.entityId,
        name = medicine.name,
        unit = medicine.unit,
        type = medicine.type
    )
}