package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException

interface DeleteMedicineUseCase {

    @Throws(MedicineNotFoundException::class)
    suspend fun execute(medicineId: String)
}