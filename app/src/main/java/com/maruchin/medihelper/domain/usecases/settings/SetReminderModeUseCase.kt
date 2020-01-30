package com.maruchin.medihelper.domain.usecases.settings

import com.maruchin.medihelper.domain.entities.ReminderMode

interface SetReminderModeUseCase {
    suspend fun execute(newMode: ReminderMode)
}