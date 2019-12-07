package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.MedicineStateData
import java.io.File

data class MedicineItem(
    val medicineId: String,
    val name: String,
    val unit: String,
    val stateData: MedicineStateData,
    val pictureName: String?
) {
    constructor(medicine: Medicine) : this(
        medicineId = medicine.medicineId,
        name = medicine.name,
        unit = medicine.unit,
        stateData = medicine.getStateData(),
        pictureName = medicine.pictureName
    )
}