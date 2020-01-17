package com.maruchin.medihelper.domain.usecasesimpl.settings

import com.maruchin.medihelper.domain.repositories.MedicineTypeRepo
import com.maruchin.medihelper.domain.usecases.saved_types.AddMedicineTypeUseCase

class AddMedicineTypeUseCaseImpl(
    private val medicineTypeRepo: MedicineTypeRepo
) : AddMedicineTypeUseCase {

    override suspend fun execute(type: String) {
        medicineTypeRepo.addNewDistinct(type)
    }
}