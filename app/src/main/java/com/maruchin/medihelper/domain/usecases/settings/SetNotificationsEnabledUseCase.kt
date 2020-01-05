package com.maruchin.medihelper.domain.usecases.settings

interface SetNotificationsEnabledUseCase {

    suspend fun execute(enabled: Boolean)
}