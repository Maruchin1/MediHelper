package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.repositories.MedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteMedicineUseCase(
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(medicineId: String) = withContext(Dispatchers.Default) {
        medicineRepo.getById(medicineId)?.pictureName?.let {
            medicineRepo.deleteMedicinePicture(it)
        }
        medicineRepo.deleteById(medicineId)
        return@withContext
    }
}