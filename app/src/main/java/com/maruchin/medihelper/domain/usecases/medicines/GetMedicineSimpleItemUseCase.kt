package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.model.MedicineSimpleItem

interface GetMedicineSimpleItemUseCase {

    suspend fun execute(medicineId: String): MedicineSimpleItem?
}