package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.entities.MedicineInfo
import com.maruchin.medihelper.domain.repositories.MedicineRepo

class GetMedicineInfoUseCase(
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(urlString: String): List<MedicineInfo> {
        return medicineRepo.getMedicineInfo(urlString)
    }
}