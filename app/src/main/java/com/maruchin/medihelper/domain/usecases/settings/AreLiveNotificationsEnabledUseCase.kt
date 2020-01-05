package com.maruchin.medihelper.domain.usecases.settings

import androidx.lifecycle.LiveData

interface AreLiveNotificationsEnabledUseCase {

    suspend fun execute(): LiveData<Boolean>
}