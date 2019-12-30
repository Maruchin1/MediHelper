package com.maruchin.medihelper.domain.usecases.medicines

interface GetMedicineUnitsUseCase {

    suspend fun execute(): List<String>
}