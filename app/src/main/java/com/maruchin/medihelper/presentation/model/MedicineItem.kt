package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.entities.MedicineStateData
import com.maruchin.medihelper.domain.usecases.medicines.GetAllMedicinesItemsLiveUseCase

data class MedicineItem(
   val medicineId: String,
   val name: String,
   val unit: String,
   val stateData: MedicineStateData
) {
   constructor(data: GetAllMedicinesItemsLiveUseCase.MedicineItem) : this(
      medicineId = data.medicineId,
      name = data.name,
      unit = data.unit,
      stateData = data.stateData
   )
}