package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.entities.PlannedMedicine

interface DeletePlannedMedicinesUseCase {

    suspend fun execute(plannedMedicines: List<PlannedMedicine>)
}