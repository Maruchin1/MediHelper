package com.maruchin.medihelper.domain.usecasesimpl.saved_types

import com.maruchin.medihelper.domain.repositories.MedicineUnitRepo
import com.maruchin.medihelper.domain.usecases.saved_types.DeleteMedicineUnitUseCase

class DeleteMedicineUnitUseCaseImpl(
    private val medicineUnitRepo: MedicineUnitRepo
) : DeleteMedicineUnitUseCase {

    override suspend fun execute(type: String) {
        medicineUnitRepo.delete(type)
    }
}