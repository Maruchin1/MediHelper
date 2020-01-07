package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineState

data class MedicineEditData(
    val medicineId: String,
    val name: String,
    val unit: String,
    val expireDate: AppExpireDate?,
    val state: MedicineState,
    val pictureName: String?
) {
    constructor(medicine: Medicine) : this(
        medicineId = medicine.entityId,
        unit = medicine.unit,
        name = medicine.name,
        expireDate = medicine.expireDate,
        state = medicine.state,
        pictureName = medicine.pictureName
    )
}