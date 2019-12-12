package com.maruchin.medihelper.data.model

import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine
import java.io.File


data class MedicineDb(
    val name: String? = null,
    val unit: String? = null,
    val expireDate: String? = null,
    val packageSize: Float? = null,
    val currState: Float? = null,
    val pictureName: String? = null
) {
    constructor(medicine: Medicine) : this(
        name = medicine.name,
        unit = medicine.unit,
        expireDate = medicine.expireDate.jsonFormatString,
        packageSize = medicine.packageSize,
        currState = medicine.currState,
        pictureName = medicine.pictureName
    )

    fun toMedicine(id: String) = Medicine(
        medicineId = id,
        name = name ?: "--",
        unit = unit ?: "--",
        expireDate = expireDate?.let { AppExpireDate(it) } ?: AppExpireDate(0, 0),
        packageSize = packageSize ?: 0f,
        currState = currState ?: 0f,
        pictureName = pictureName
    )
}