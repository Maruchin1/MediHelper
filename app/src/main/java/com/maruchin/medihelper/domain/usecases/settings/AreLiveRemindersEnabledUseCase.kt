package com.maruchin.medihelper.domain.usecases.settings

import androidx.lifecycle.LiveData

interface AreLiveRemindersEnabledUseCase {

    suspend fun execute(): LiveData<Boolean>
}