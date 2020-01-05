package com.maruchin.medihelper.domain.repositories

import androidx.lifecycle.LiveData

interface SettingsRepo {

    suspend fun areNotificationsEnabled(): Boolean
    suspend fun areAlarmsEnabled(): Boolean
    suspend fun areLiveNotificationsEnabled(): LiveData<Boolean>
    suspend fun areLiveAlarmsEnabled(): LiveData<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun setAlarmsEnabled(enabled: Boolean)
}