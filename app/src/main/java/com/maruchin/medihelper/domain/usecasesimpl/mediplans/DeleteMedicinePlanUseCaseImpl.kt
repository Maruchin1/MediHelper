package com.maruchin.medihelper.domain.usecasesimpl.mediplans

import com.maruchin.medihelper.domain.device.DeviceReminder
import com.maruchin.medihelper.domain.repositories.MedicinePlanRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.mediplans.DeleteMedicinePlanUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteMedicinePlanUseCaseImpl(
    private val medicinePlanRepo: MedicinePlanRepo,
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val deviceReminder: DeviceReminder
) : DeleteMedicinePlanUseCase {

    override suspend fun execute(medicinePlanId: String) = withContext(Dispatchers.Default) {
        val plannedMedicines = plannedMedicineRepo.getListByMedicinePlan(medicinePlanId)
        val plannedMedicinesIds = plannedMedicines.map { it.entityId }

        medicinePlanRepo.deleteById(medicinePlanId)
        plannedMedicineRepo.deleteListById(plannedMedicinesIds)
        deviceReminder.cancelReminders(plannedMedicines)

        return@withContext
    }
}