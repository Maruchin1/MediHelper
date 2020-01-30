package com.maruchin.medihelper.domain.usecases.settings

import com.maruchin.medihelper.domain.entities.ReminderMode

interface GetReminderModeUseCase {
    suspend fun execute(): ReminderMode
}