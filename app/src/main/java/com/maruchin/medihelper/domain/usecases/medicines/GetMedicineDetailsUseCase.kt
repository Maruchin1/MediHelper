package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.model.MedicineDetails
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException

interface GetMedicineDetailsUseCase {

    @Throws(MedicineNotFoundException::class)
    suspend fun execute(medicineId: String): MedicineDetails
}