package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.data.utils.DataSharedPref
import com.maruchin.medihelper.data.utils.SettingsSharedPref
import com.maruchin.medihelper.domain.repositories.SettingsRepo

class SettingsRepoImpl(
    private val settingsSharedPref: SettingsSharedPref
) : SettingsRepo {

    override suspend fun areNotificationsEnabled(): Boolean {
        return settingsSharedPref.getNotificationsEnabled()
    }

    override suspend fun areAlarmsEnabled(): Boolean {
        return settingsSharedPref.getAlarmsEnabled()
    }

    override suspend fun areLiveNotificationsEnabled(): LiveData<Boolean> {
        return settingsSharedPref.getLiveNotificationsEnabled()
    }

    override suspend fun areLiveAlarmsEnabled(): LiveData<Boolean> {
        return settingsSharedPref.getLiveAlarmsEnabled()
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        settingsSharedPref.setNotificationsEnabled(enabled)
    }

    override suspend fun setAlarmsEnabled(enabled: Boolean) {
        settingsSharedPref.setAlarmsEnabled(enabled)
    }


}