package com.maruchin.medihelper.domain.usecases.plannedmedicines

interface SetPlannedMedicineTakenUseCase {

    suspend fun execute(plannedMedicineId: String)
}