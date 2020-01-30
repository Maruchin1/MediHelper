package com.maruchin.medihelper.domain.usecasesimpl.saved_types

import com.maruchin.medihelper.domain.repositories.MedicineTypeRepo
import com.maruchin.medihelper.domain.usecases.saved_types.DeleteMedicineTypeUseCase

class DeleteMedicineTypeUseCaseImpl(
    private val medicineTypeRepo: MedicineTypeRepo
) : DeleteMedicineTypeUseCase {

    override suspend fun execute(type: String) {
        medicineTypeRepo.delete(type)
    }
}