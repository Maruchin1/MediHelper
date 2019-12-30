package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.entities.MedicineInfoSearchResult
import com.maruchin.medihelper.domain.repositories.MedicineRepo

interface SearchForMedicineInfoUseCase {

    suspend fun execute(medicineName: String): List<MedicineInfoSearchResult>
}