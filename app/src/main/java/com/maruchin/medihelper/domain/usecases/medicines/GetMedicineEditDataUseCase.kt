package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.model.MedicineEditData
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException

interface GetMedicineEditDataUseCase {

    @Throws(MedicineNotFoundException::class)
    suspend fun execute(medicineId: String): MedicineEditData
}