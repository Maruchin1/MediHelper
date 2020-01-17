package com.maruchin.medihelper.domain.usecasesimpl.settings

import com.maruchin.medihelper.domain.repositories.MedicineUnitRepo
import com.maruchin.medihelper.domain.usecases.settings.DeleteSavedMedicineUnitUseCase

class DeleteSavedMedicineUnitUseCaseImpl(
    private val medicineUnitRepo: MedicineUnitRepo
) : DeleteSavedMedicineUnitUseCase {

    override suspend fun execute(type: String) {
        medicineUnitRepo.delete(type)
    }
}