package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.DeleteMedicineUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteMedicineUseCaseImpl(
    private val medicineRepo: MedicineRepo
) : DeleteMedicineUseCase {

    override suspend fun execute(medicineId: String) = withContext(Dispatchers.Default) {
        medicineRepo.getById(medicineId)?.pictureName?.let {
            medicineRepo.deleteMedicinePicture(it)
        }
        medicineRepo.deleteById(medicineId)
        return@withContext
    }
}