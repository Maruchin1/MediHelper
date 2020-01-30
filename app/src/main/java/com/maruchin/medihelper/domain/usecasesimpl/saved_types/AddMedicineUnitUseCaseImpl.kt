package com.maruchin.medihelper.domain.usecasesimpl.saved_types

import com.maruchin.medihelper.domain.repositories.MedicineUnitRepo
import com.maruchin.medihelper.domain.usecases.saved_types.AddMedicineUnitUseCase

class AddMedicineUnitUseCaseImpl(
    private val medicineUnitRepo: MedicineUnitRepo
) : AddMedicineUnitUseCase {

    override suspend fun execute(type: String) {
        medicineUnitRepo.addNewDistinct(type)
    }
}