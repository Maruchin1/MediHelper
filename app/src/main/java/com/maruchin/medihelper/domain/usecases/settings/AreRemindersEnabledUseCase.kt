package com.maruchin.medihelper.domain.usecases.settings

interface AreRemindersEnabledUseCase {

    suspend fun execute(): Boolean
}