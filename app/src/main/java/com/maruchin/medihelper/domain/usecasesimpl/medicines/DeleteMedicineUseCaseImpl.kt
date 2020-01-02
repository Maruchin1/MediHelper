package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.DeleteMedicineUseCase
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteMedicineUseCaseImpl(
    private val medicineRepo: MedicineRepo
) : DeleteMedicineUseCase {

    override suspend fun execute(medicineId: String) = withContext(Dispatchers.Default) {
        val medicine = getMedicine(medicineId)
        if (medicine.pictureName != null) {
            medicineRepo.deleteMedicinePicture(medicine.pictureName)
        }
        medicineRepo.deleteById(medicineId)
        return@withContext
    }

    private suspend fun getMedicine(medicineId: String): Medicine {
        return medicineRepo.getById(medicineId) ?: throw MedicineNotFoundException()
    }
}