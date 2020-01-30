package com.maruchin.medihelper.domain.usecases.settings

interface SetRemindersEnabledUseCase {

    suspend fun execute(enabled: Boolean)
}