package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.entities.AppTime

interface ChangePlannedMedicineTimeUseCase {

    suspend fun execute(plannedMedicineId: String, newPlannedTime: AppTime)
}