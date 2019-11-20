package com.example.medihelper.presentation.model

import com.example.medihelper.domain.entities.AppExpireDate
import com.example.medihelper.domain.entities.Medicine

data class MedicineShortInfo(
    val name: String,
    val unit: String,
    val expireDate: AppExpireDate
) {
    constructor(medicine: Medicine) : this(
        name = medicine.name,
        unit = medicine.unit,
        expireDate = medicine.expireDate
    )
}