package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.repositories.MedicineRepo

class DeleteMedicineUseCase(
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(medicineId: String) {
        medicineRepo.getById(medicineId)?.pictureName?.let {
            medicineRepo.deleteMedicinePicture(it)
        }
        medicineRepo.deleteById(medicineId)
    }
}