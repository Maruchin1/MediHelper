package com.maruchin.medihelper.domain.usecases.medicines

import com.maruchin.medihelper.domain.model.MedicineDefaults

interface GetMedicineDefaultsUseCase {

    suspend fun execute(): MedicineDefaults
}