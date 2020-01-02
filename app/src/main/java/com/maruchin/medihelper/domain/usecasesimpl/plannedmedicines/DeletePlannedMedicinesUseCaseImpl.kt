package com.maruchin.medihelper.domain.usecasesimpl.plannedmedicines

import com.maruchin.medihelper.domain.device.DeviceReminder
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.plannedmedicines.DeletePlannedMedicinesUseCase

class DeletePlannedMedicinesUseCaseImpl(
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val deviceReminder: DeviceReminder
) : DeletePlannedMedicinesUseCase {

    override suspend fun execute(plannedMedicines: List<PlannedMedicine>) {
        plannedMedicineRepo.deleteListById(plannedMedicines.map { it.entityId })
        deviceReminder.cancelReminders(plannedMedicines)
    }
}