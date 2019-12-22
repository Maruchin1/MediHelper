package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.entities.MedicineInfo
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMedicineInfoUseCase(
    private val medicineRepo: MedicineRepo
) {
    suspend fun execute(urlString: String): List<MedicineInfo> {
        return medicineRepo.getMedicineInfo(urlString)
    }
}