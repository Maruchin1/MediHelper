package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.model.MedicineEditData
import com.maruchin.medihelper.domain.repositories.MedicineRepo

class GetMedicineEditDataUseCase(
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(medicineId: String): MedicineEditData? {
        return medicineRepo.getById(medicineId)?.let { medicine ->
            MedicineEditData(medicine)
        }
    }
}