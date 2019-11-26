package com.maruchin.medihelper.data.model

import com.maruchin.medihelper.data.framework.FirebaseEntity
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine


data class MedicineDb(
    val name: String? = null,
    val unit: String? = null,
    val expireDate: String? = null,
    val packageSize: Float? = null,
    val currState: Float? = null,
    val additionalInfo: String? = null
) : FirebaseEntity<Medicine> {

    constructor(medicine: Medicine) : this(
        name = medicine.name,
        unit = medicine.unit,
        expireDate = medicine.expireDate.jsonFormatString,
        packageSize = medicine.packageSize,
        currState = medicine.currState,
        additionalInfo = medicine.additionalInfo
    )

    override fun toDomainEntity(id: String): Medicine {
        return Medicine(
            medicineId = id,
            name = name ?: "--",
            unit = unit ?: "--",
            expireDate = expireDate?.let { AppExpireDate(it) } ?: AppExpireDate(0, 0),
            packageSize = packageSize,
            currState = currState,
            additionalInfo = additionalInfo
        )
    }
}