package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineState
import com.maruchin.medihelper.domain.entities.Profile

data class MedicineDetails(
    val medicineId: String,
    val name: String,
    val unit: String,
    val expireDate: AppExpireDate?,
    val daysRemains: Int?,
    val state: MedicineState,
    val pictureName: String?,
    val profileItems: List<ProfileItem>
) {
    constructor(medicine: Medicine, daysRemains: Int?, profileList: List<Profile>) : this(
        medicineId = medicine.entityId,
        name = medicine.name,
        unit = medicine.unit,
        expireDate = medicine.expireDate,
        daysRemains = daysRemains,
        state = medicine.state,
        pictureName = medicine.pictureName,
        profileItems = profileList.map {
            ProfileItem(
                profileId = it.entityId,
                name = it.name,
                color = it.color,
                mainPerson = it.mainPerson
            )
        }
    )
}