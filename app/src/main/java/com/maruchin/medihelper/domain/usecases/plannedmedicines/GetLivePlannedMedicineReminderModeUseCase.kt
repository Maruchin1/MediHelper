package com.maruchin.medihelper.domain.usecases.plannedmedicines

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.ReminderMode

interface GetLivePlannedMedicineReminderModeUseCase {

    suspend fun execute(): LiveData<ReminderMode>
}