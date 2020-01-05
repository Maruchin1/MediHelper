package com.maruchin.medihelper.domain.usecasesimpl.settings

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.repositories.SettingsRepo
import com.maruchin.medihelper.domain.usecases.settings.AreLiveNotificationsEnabledUseCase

class AreLiveNotificationsEnabledUseCaseImpl(
    private val settingsRepo: SettingsRepo
) : AreLiveNotificationsEnabledUseCase {

    override suspend fun execute(): LiveData<Boolean> {
        return settingsRepo.areLiveNotificationsEnabled()
    }
}