package com.maruchin.medihelper.domain.usecasesimpl.settings

import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.repositories.SettingsRepo
import com.maruchin.medihelper.domain.usecases.settings.SetReminderModeUseCase

class SetReminderModeUseCaseImpl(
    private val settingsRepo: SettingsRepo
) : SetReminderModeUseCase {

    override suspend fun execute(newMode: ReminderMode) {
        return settingsRepo.setReminderMode(newMode)
    }
}