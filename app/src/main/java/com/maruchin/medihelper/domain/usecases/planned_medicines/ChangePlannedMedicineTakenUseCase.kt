package com.maruchin.medihelper.domain.usecases.planned_medicines

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.usecases.MedicineNotFoundException
import com.maruchin.medihelper.domain.usecases.PlannedMedicineNotFoundException


interface ChangePlannedMedicineTakenUseCase {

    @Throws(
        PlannedMedicineNotFoundException::class,
        MedicineNotFoundException::class
    )
    suspend fun execute(params: Params)

    data class Params(
        val planId: String,
        val plannedDate: AppDate,
        val plannedTime: AppTime
    )
}