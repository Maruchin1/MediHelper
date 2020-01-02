package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.model.PlannedMedicineDetails
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.PlannedMedicineNotFoundException

interface GetPlannedMedicineDetailsUseCase {

    @Throws(
        PlannedMedicineNotFoundException::class,
        MedicineNotFoundException::class
    )
    suspend fun execute(plannedMedicineId: String): PlannedMedicineDetails
}