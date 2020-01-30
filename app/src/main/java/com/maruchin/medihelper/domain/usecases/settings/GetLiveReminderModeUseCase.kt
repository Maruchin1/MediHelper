package com.maruchin.medihelper.domain.usecases.settings

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.ReminderMode

interface GetLiveReminderModeUseCase {
    suspend fun execute(): LiveData<ReminderMode>
}