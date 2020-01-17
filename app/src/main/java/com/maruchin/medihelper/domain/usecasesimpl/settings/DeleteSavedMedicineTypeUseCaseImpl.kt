package com.maruchin.medihelper.domain.usecasesimpl.settings

import com.maruchin.medihelper.domain.repositories.MedicineTypeRepo
import com.maruchin.medihelper.domain.usecases.settings.DeleteSavedMedicineTypeUseCase

class DeleteSavedMedicineTypeUseCaseImpl(
    private val medicineTypeRepo: MedicineTypeRepo
) : DeleteSavedMedicineTypeUseCase {

    override suspend fun execute(type: String) {
        medicineTypeRepo.delete(type)
    }
}