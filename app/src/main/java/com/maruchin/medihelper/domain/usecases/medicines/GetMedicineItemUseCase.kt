package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.model.MedicineItem
import com.maruchin.medihelper.domain.repositories.MedicineRepo

class GetMedicineItemUseCase(
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(medicineId: String): MedicineItem? {
        val medicine = medicineRepo.getById(medicineId)
        return medicine?.let { MedicineItem(it) }
    }
}