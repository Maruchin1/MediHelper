package com.maruchin.medihelper.domain.usecasesimpl.mediplans

import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.mediplans.DeleteSingleMedicinePlanUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.DeletePlannedMedicinesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteSingleMedicinePlanUseCaseImpl(
    private val medicinePlanRepo: MedicinePlanRepo,
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val deletePlannedMedicinesUseCase: DeletePlannedMedicinesUseCase
) : DeleteSingleMedicinePlanUseCase {

    override suspend fun execute(medicinePlanId: String) = withContext(Dispatchers.Default) {
        val plannedMedicines = plannedMedicineRepo.getListByMedicinePlan(medicinePlanId)
        medicinePlanRepo.deleteById(medicinePlanId)
        deletePlannedMedicinesUseCase.execute(plannedMedicines)
        return@withContext
    }
}