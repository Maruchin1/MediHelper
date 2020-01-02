package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.model.MedicineSimpleItem
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException

interface GetMedicineSimpleItemUseCase {

    @Throws(MedicineNotFoundException::class)
    suspend fun execute(medicineId: String): MedicineSimpleItem
}