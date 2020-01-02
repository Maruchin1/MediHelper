package com.maruchin.medihelper.domain.usecasesimpl.plannedmedicines

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.repositories.SettingsRepo
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetLivePlannedMedicineReminderModeUseCase

class GetLivePlannedMedicineReminderModeUseCaseImpl(
    private val settingsRepo: SettingsRepo
) : GetLivePlannedMedicineReminderModeUseCase {

    override suspend fun execute(): LiveData<ReminderMode> {
        return settingsRepo.getLiveReminderMode()
    }
}