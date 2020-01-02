package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.entities.ReminderMode

interface ChangePlannedMedicineReminderModeUseCase {

    suspend fun execute(newMode: ReminderMode)
}