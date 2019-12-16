package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.repositories.MedicineRepo

class GetMedicineNameUseCase(
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(medicineId: String): String? {
        val medicine = medicineRepo.getById(medicineId)
        return medicine?.name
    }
}