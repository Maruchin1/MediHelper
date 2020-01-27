package com.maruchin.medihelper.domain.usecases.planned_medicines

import com.maruchin.medihelper.domain.entities.TakenMedicine
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.PlannedMedicineNotFoundException


interface ChangePlannedMedicineTakenUseCase {

    @Throws(
        PlannedMedicineNotFoundException::class,
        MedicineNotFoundException::class
    )
    suspend fun execute(takenMedicine: TakenMedicine)
}