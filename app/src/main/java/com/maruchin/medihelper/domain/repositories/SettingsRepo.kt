package com.maruchin.medihelper.domain.repositories

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.ReminderMode

interface SettingsRepo {

    fun setReminderMode(mode: ReminderMode)
    fun getReminderMode(): ReminderMode
    fun getLiveReminderMode(): LiveData<ReminderMode>
}