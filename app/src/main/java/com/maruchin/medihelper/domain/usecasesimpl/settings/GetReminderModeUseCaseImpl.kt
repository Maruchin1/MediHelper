package com.maruchin.medihelper.domain.usecasesimpl.settings

import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.repositories.SettingsRepo
import com.maruchin.medihelper.domain.usecases.settings.GetReminderModeUseCase

class GetReminderModeUseCaseImpl(
    private val settingsRepo: SettingsRepo
) : GetReminderModeUseCase {

    override suspend fun execute(): ReminderMode {
        return settingsRepo.getReminderMode()
    }
}