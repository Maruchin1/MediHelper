package com.maruchin.medihelper.domain.usecasesimpl.settings

import com.maruchin.medihelper.domain.repositories.MedicineTypeRepo
import com.maruchin.medihelper.domain.usecases.saved_types.DeleteMedicineTypeUseCase

class DeleteMedicineTypeUseCaseImpl(
    private val medicineTypeRepo: MedicineTypeRepo
) : DeleteMedicineTypeUseCase {

    override suspend fun execute(type: String) {
        medicineTypeRepo.delete(type)
    }
}