package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.model.MedicineEditData
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMedicineEditDataUseCase(
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(medicineId: String): MedicineEditData? = withContext(Dispatchers.Default) {
        return@withContext medicineRepo.getById(medicineId)?.let { medicine ->
            MedicineEditData(medicine)
        }
    }
}