package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.entities.MedicineInfoSearchResult
import com.maruchin.medihelper.domain.repositories.MedicineRepo

class SearchForMedicineInfoUseCase(
    private val medicineRepo: MedicineRepo
) {

    suspend fun execute(medicineName: String): List<MedicineInfoSearchResult> {
        return medicineRepo.searchForMedicineInfo(medicineName)
    }
}