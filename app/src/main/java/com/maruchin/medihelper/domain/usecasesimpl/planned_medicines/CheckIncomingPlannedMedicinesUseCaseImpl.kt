package com.maruchin.medihelper.domain.usecasesimpl.planned_medicines

import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.usecases.planned_medicines.CheckIncomingPlannedMedicinesUseCase

class CheckIncomingPlannedMedicinesUseCaseImpl(
    private val deviceCalendar: DeviceCalendar,
    private val planRepo: PlanRepo
) : CheckIncomingPlannedMedicinesUseCase {

    override suspend fun execute() {
        val currDate = deviceCalendar.getCurrDate()

    }
}