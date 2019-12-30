package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.entities.MedicineInfoSearchResult
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.SearchForMedicineInfoUseCase

class SearchForMedicineInfoUseCaseImpl(
    private val medicineRepo: MedicineRepo
) : SearchForMedicineInfoUseCase {

    override suspend fun execute(medicineName: String): List<MedicineInfoSearchResult> {
        return medicineRepo.searchForMedicineInfo(medicineName)
    }
}