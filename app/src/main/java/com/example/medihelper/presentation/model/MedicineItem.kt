package com.example.medihelper.presentation.model

import com.example.medihelper.domain.entities.Medicine
import com.example.medihelper.domain.entities.MedicineStateData
import java.io.File

data class MedicineItem(
   val medicineId: Int,
   val name: String,
   val unit: String,
   val stateData: MedicineStateData,
   val image: File?
) {
   constructor(medicine: Medicine) : this(
      medicineId = medicine.medicineId,
      name = medicine.name,
      unit = medicine.unit,
      stateData = medicine.getStateData(),
      image = medicine.image
   )
}