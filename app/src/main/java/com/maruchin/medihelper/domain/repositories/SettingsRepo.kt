package com.maruchin.medihelper.domain.repositories

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.ReminderMode

interface SettingsRepo {

    suspend fun areRemindersEnabled(): Boolean
    suspend fun areLiveRemindersEnabled(): LiveData<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun getReminderMode(): ReminderMode
    suspend fun getLiveReminderMode(): LiveData<ReminderMode>
    suspend fun setReminderMode(newMode: ReminderMode)
}