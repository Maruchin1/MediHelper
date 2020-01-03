package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.model.MedicineItem

data class MedicineItemData(
    val medicineId: String,
    val pictureName: String?,
    val medicineName: String,
    val medicineUnit: String,
    val stateData: MedicineStateData
) {
    companion object {

        fun fromDomainModel(medicineItem: MedicineItem): MedicineItemData {
            return MedicineItemData(
                medicineId = medicineItem.medicineId,
                pictureName = medicineItem.pictureName,
                medicineName = medicineItem.name,
                medicineUnit = medicineItem.unit,
                stateData = MedicineStateData.fromDomainModel(
                    medicineState = medicineItem.state,
                    medicineUnit = medicineItem.unit
                )
            )
        }
    }
}