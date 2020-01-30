package com.maruchin.medihelper.domain.usecasesimpl.settings

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.repositories.SettingsRepo
import com.maruchin.medihelper.domain.usecases.settings.GetLiveReminderModeUseCase

class GetLiveReminderModeUseCaseImpl(
    private val settingsRepo: SettingsRepo
) : GetLiveReminderModeUseCase {

    override suspend fun execute(): LiveData<ReminderMode> {
        return settingsRepo.getLiveReminderMode()
    }
}