package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineUnitsUseCase

class GetMedicineUnitsUseCaseImpl(
    private val medicineRepo: MedicineRepo
) : GetMedicineUnitsUseCase {

    override suspend fun execute(): List<String> {
        return medicineRepo.getMedicineUnits()
    }
}