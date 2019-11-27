package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine

data class MedicineEditData(
    val medicineId: String,
    val name: String,
    val unit: String,
    val expireDate: AppExpireDate,
    val packageSize: Float?,
    val currState: Float?,
    val additionalInfo: String?
) {
    constructor(medicine: Medicine) : this(
        medicineId = medicine.medicineId,
        unit = medicine.unit,
        name = medicine.name,
        expireDate = medicine.expireDate,
        packageSize = medicine.packageSize,
        currState = medicine.currState,
        additionalInfo = medicine.additionalInfo
    )
}