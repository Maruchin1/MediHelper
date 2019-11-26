package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.MedicineStateData
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineDetailsUseCase

data class MedicineDetails(
    val medicineId: String,
    val name: String,
    val unit: String,
    val expireDate: AppExpireDate?,
    val stateData: MedicineStateData,
    val additionalInfoAvailable: Boolean,
    val additionalInfo: String?,
    val profileItemListAvailable: Boolean,
    val profileItemList: List<ProfileSimpleItem>
) {
    constructor(data:GetMedicineDetailsUseCase.MedicineDetails) : this(
        medicineId = data.medicineId,
        name = data.name,
        unit = data.unit,
        expireDate = data.expireDate,
        stateData = data.stateData,
        additionalInfoAvailable = data.additionalInfo != null,
        additionalInfo = data.additionalInfo,
        profileItemListAvailable = !data.profileSimpleItemList.isNullOrEmpty(),
        profileItemList = data.profileSimpleItemList.map { ProfileSimpleItem(it) }
    )
}