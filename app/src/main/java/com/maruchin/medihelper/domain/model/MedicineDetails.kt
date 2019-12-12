package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineStateData
import com.maruchin.medihelper.domain.entities.Profile
import java.io.File

data class MedicineDetails(
    val medicineId: String,
    val name: String,
    val unit: String,
    val expireDate: AppExpireDate,
    val stateData: MedicineStateData,
    val pictureName: String?,
    val profileSimpleItemList: List<ProfileSimpleItem>
) {
    constructor(medicine: Medicine, profileList: List<Profile>) : this(
        medicineId = medicine.medicineId,
        name = medicine.name,
        unit = medicine.unit,
        expireDate = medicine.expireDate,
        stateData = medicine.stateData,
        pictureName = medicine.pictureName,
        profileSimpleItemList = profileList.map {
            ProfileSimpleItem(
                profileId = it.profileId,
                name = it.name,
                color = it.color
            )
        }
    )
}