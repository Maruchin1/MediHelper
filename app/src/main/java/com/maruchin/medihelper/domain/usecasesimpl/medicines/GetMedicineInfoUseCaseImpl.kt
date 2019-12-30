package com.maruchin.medihelper.domain.usecasesimpl.medicines

import com.maruchin.medihelper.domain.entities.MedicineInfo
import com.maruchin.medihelper.domain.repositories.MedicineRepo
import com.maruchin.medihelper.domain.usecases.medicines.GetMedicineInfoUseCase

class GetMedicineInfoUseCaseImpl(
    private val medicineRepo: MedicineRepo
) : GetMedicineInfoUseCase {

    override suspend fun execute(urlString: String): List<MedicineInfo> {
        return medicineRepo.getMedicineInfo(urlString)
    }
}