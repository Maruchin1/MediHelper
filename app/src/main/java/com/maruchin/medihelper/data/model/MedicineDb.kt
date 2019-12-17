package com.maruchin.medihelper.data.model

import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine


data class MedicineDb(
    val name: String? = null,
    val unit: String? = null,
    val expireDate: String? = null,
    val packageSize: Float? = null,
    val currState: Float? = null,
    val pictureName: String? = null
) {
    constructor(entity: Medicine) : this(
        name = entity.name,
        unit = entity.unit,
        expireDate = entity.expireDate.jsonFormatString,
        packageSize = entity.packageSize,
        currState = entity.currState,
        pictureName = entity.pictureName
    )

    fun toEntity(id: String) = Medicine(
        medicineId = id,
        name = name!!,
        unit = unit!!,
        expireDate = AppExpireDate(expireDate!!),
        packageSize = packageSize!!,
        currState = currState!!,
        pictureName = pictureName
    )
}