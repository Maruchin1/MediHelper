package com.maruchin.medihelper.domain.usecases.settings

interface AreNotificationsEnabledUseCase {

    suspend fun execute(): Boolean
}