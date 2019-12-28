package com.maruchin.medihelper.domain.usecases.plannedmedicines

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.repositories.SettingsRepo

class GetLivePlannedMedicineReminderModeUseCase(
    private val settingsRepo: SettingsRepo
) {
    suspend fun execute(): LiveData<ReminderMode> {
        return settingsRepo.getLiveReminderMode()
    }
}