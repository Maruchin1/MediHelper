package com.maruchin.medihelper.domain.repositories

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.ReminderMode

interface SettingsRepo {

    suspend fun setReminderMode(mode: ReminderMode)
    suspend fun getReminderMode(): ReminderMode
    suspend fun getLiveReminderMode(): LiveData<ReminderMode>
}