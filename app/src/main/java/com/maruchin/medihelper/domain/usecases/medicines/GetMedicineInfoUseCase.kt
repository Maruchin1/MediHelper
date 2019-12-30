package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.entities.MedicineInfo

interface GetMedicineInfoUseCase {

    suspend fun execute(urlString: String): List<MedicineInfo>
}