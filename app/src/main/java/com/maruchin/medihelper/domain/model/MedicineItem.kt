package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineState

data class MedicineItem(
    val medicineId: String,
    val name: String,
    val expireDate: AppExpireDate?,
    val type: String?,
    val state: MedicineState,
    val pictureName: String?
) {
    constructor(medicine: Medicine) : this(
        medicineId = medicine.entityId,
        name = medicine.name,
        expireDate = medicine.expireDate,
        type = medicine.type,
        state = medicine.state,
        pictureName = medicine.pictureName
    )
}