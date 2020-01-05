package com.maruchin.medihelper.domain.usecasesimpl.settings

import com.maruchin.medihelper.domain.repositories.SettingsRepo
import com.maruchin.medihelper.domain.usecases.settings.AreNotificationsEnabledUseCase

class AreNotificationsEnabledUseCaseImpl(
    private val settingsRepo: SettingsRepo
) : AreNotificationsEnabledUseCase {

    override suspend fun execute(): Boolean {
        return settingsRepo.areNotificationsEnabled()
    }
}