package com.maruchin.medihelper.domain.usecasesimpl.settings

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.repositories.SettingsRepo
import com.maruchin.medihelper.domain.usecases.settings.AreLiveRemindersEnabledUseCase

class AreLiveRemindersEnabledUseCaseImpl(
    private val settingsRepo: SettingsRepo
) : AreLiveRemindersEnabledUseCase {

    override suspend fun execute(): LiveData<Boolean> {
        return settingsRepo.areLiveRemindersEnabled()
    }
}