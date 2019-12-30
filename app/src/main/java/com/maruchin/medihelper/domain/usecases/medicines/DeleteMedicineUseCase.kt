package com.maruchin.medihelper.domain.usecases.medicines

interface DeleteMedicineUseCase {

    suspend fun execute(medicineId: String)
}