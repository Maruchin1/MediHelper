package com.maruchin.medihelper.domain.usecases.plannedmedicines

import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.repositories.SettingsRepo

class ChangePlannedMedicineReminderModeUseCase(
    private val settingsRepo: SettingsRepo
) {
    suspend fun execute(newMode: ReminderMode) {
        settingsRepo.setReminderMode(newMode)
    }
}