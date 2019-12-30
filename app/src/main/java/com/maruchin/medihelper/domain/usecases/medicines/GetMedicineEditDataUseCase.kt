package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.model.MedicineEditData

interface GetMedicineEditDataUseCase {

    suspend fun execute(medicineId: String): MedicineEditData?
}