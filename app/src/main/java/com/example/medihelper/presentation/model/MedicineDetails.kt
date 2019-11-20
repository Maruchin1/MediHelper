package com.example.medihelper.presentation.model

import com.example.medihelper.domain.entities.AppExpireDate
import com.example.medihelper.domain.entities.Medicine
import com.example.medihelper.domain.entities.MedicineStateData
import java.io.File

data class MedicineDetails(
    val medicineId: Int,
    val name: String,
    val unit: String,
    val expireDate: AppExpireDate?,
    val stateData: MedicineStateData,
    val additionalInfoAvailable: Boolean,
    val additionalInfo: String?,
    val image: File?
) {
    constructor(medicine: Medicine) : this(
        medicineId = medicine.medicineId,
        name = medicine.name,
        unit = medicine.unit,
        expireDate = medicine.expireDate,
        stateData = medicine.getStateData(),
        additionalInfoAvailable = !medicine.additionalInfo.isNullOrEmpty(),
        additionalInfo = medicine.additionalInfo,
        image = medicine.image
    )
}