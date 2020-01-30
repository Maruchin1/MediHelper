package com.maruchin.medihelper.domain.usecasesimpl.settings

import com.maruchin.medihelper.domain.device.DeviceReminder
import com.maruchin.medihelper.domain.repositories.SettingsRepo
import com.maruchin.medihelper.domain.usecases.settings.SetNotificationsEnabledUseCase

class SetNotificationsEnabledUseCaseImpl(
    private val settingsRepo: SettingsRepo,
    private val deviceReminder: DeviceReminder
) : SetNotificationsEnabledUseCase {

    override suspend fun execute(enabled: Boolean) {
        if (isEnabledDifferent(enabled)) {
            settingsRepo.setNotificationsEnabled(enabled)
            deviceReminder.setupPlannedMedicinesReminders()
        }
    }

    private suspend fun isEnabledDifferent(enabled: Boolean): Boolean {
        val currEnabled = settingsRepo.areNotificationsEnabled()
        return currEnabled != enabled
    }
}