package com.maruchin.medihelper.domain.usecasesimpl.settings

import com.maruchin.medihelper.domain.repositories.SettingsRepo
import com.maruchin.medihelper.domain.usecases.settings.AreRemindersEnabledUseCase

class AreRemindersEnabledUseCaseImpl(
    private val settingsRepo: SettingsRepo
) : AreRemindersEnabledUseCase {

    override suspend fun execute(): Boolean {
        return settingsRepo.areRemindersEnabled()
    }
}