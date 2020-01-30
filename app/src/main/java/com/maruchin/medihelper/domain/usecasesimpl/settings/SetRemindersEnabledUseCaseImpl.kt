package com.maruchin.medihelper.domain.usecasesimpl.settings

import com.maruchin.medihelper.domain.device.DeviceReminder
import com.maruchin.medihelper.domain.repositories.SettingsRepo
import com.maruchin.medihelper.domain.usecases.settings.SetRemindersEnabledUseCase

class SetRemindersEnabledUseCaseImpl(
    private val settingsRepo: SettingsRepo,
    private val deviceReminder: DeviceReminder
) : SetRemindersEnabledUseCase {

    override suspend fun execute(enabled: Boolean) {
        if (isEnabledDifferent(enabled)) {
            settingsRepo.setNotificationsEnabled(enabled)
            deviceReminder.setupPlannedMedicinesReminders()
        }
    }

    private suspend fun isEnabledDifferent(enabled: Boolean): Boolean {
        val currEnabled = settingsRepo.areRemindersEnabled()
        return currEnabled != enabled
    }
}