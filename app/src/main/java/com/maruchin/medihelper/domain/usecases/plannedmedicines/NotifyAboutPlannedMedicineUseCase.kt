package com.maruchin.medihelper.domain.usecases.plannedmedicines

interface NotifyAboutPlannedMedicineUseCase {

    suspend fun execute(plannedMedicineId: String)
}