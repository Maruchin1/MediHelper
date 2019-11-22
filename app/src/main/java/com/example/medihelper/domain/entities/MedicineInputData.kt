package com.example.medihelper.domain.entities

import java.io.File

data class MedicineInputData(
    val name: String,
    val unit: String,
    val expireDate: AppExpireDate,
    val packageSize: Float?,
    var currState: Float?,
    val additionalInfo: String?,
    val image: File?
)