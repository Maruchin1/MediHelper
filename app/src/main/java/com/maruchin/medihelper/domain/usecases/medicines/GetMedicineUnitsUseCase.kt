package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.repositories.MedicineRepo

class GetMedicineUnitsUseCase(
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(): List<String> {
        return medicineRepo.getMedicineUnits()
    }
}