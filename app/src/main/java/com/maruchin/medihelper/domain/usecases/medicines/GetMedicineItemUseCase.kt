package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.model.MedicineItem
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMedicineItemUseCase(
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(medicineId: String): MedicineItem? = withContext(Dispatchers.Default) {
        val medicine = medicineRepo.getById(medicineId)
        return@withContext medicine?.let { MedicineItem(it) }
    }
}