package com.maruchin.medihelper.domain.usecasesimpl.plannedmedicines

import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.repositories.SettingsRepo
import com.maruchin.medihelper.domain.usecases.plannedmedicines.ChangePlannedMedicineReminderModeUseCase

class ChangePlannedMedicineReminderModeUseCaseImpl(
    private val settingsRepo: SettingsRepo
) : ChangePlannedMedicineReminderModeUseCase {

    override suspend fun execute(newMode: ReminderMode) {
        settingsRepo.setReminderMode(newMode)
    }
}