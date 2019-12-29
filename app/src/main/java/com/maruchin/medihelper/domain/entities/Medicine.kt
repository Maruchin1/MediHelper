package com.maruchin.medihelper.domain.entities

import com.maruchin.medihelper.domain.framework.BaseEntity


data class Medicine(
    override val entityId: String,
    val name: String,
    val unit: String,
    val expireDate: AppExpireDate,
    val state: MedicineState,
    val pictureName: String?
) : BaseEntity()