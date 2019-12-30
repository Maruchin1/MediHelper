package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.model.MedicineDetails

interface GetMedicineDetailsUseCase {

    suspend fun execute(medicineId: String): MedicineDetails?
}