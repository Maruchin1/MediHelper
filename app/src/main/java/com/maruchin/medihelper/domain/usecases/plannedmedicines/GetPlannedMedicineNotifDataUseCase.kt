package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.model.PlannedMedicineNotifData

interface GetPlannedMedicineNotifDataUseCase {

    suspend fun execute(plannedMedicineId: String): PlannedMedicineNotifData
}