package com.maruchin.medihelper.data.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import com.maruchin.medihelper.data.framework.SharedPrefLiveData

class SettingsSharedPref(context: Context) {

    companion object {
        private const val PREF_NAME = "settings-shared-pref"
        private const val KEY_NOTIFICATIONS_ENABLED = "key-not-taken-medicine-notifications-enabled"
        private const val KEY_ALARMS_ENABLED = "key-alarms-enabled"
    }

    private val pref: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getNotificationsEnabled(): Boolean {
        return pref.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
    }

    fun getAlarmsEnabled(): Boolean {
        return pref.getBoolean(KEY_ALARMS_ENABLED, true)
    }

    fun getLiveNotificationsEnabled(): LiveData<Boolean> {
        return SharedPrefLiveData(pref, KEY_NOTIFICATIONS_ENABLED, defaultValue = true)
    }

    fun getLiveAlarmsEnabled(): LiveData<Boolean> {
        return SharedPrefLiveData(pref, KEY_ALARMS_ENABLED, defaultValue = true)
    }

    fun setNotificationsEnabled(enabled: Boolean) = pref.edit(commit = true) {
        putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled)
    }

    fun setAlarmsEnabled(enabled: Boolean) = pref.edit(commit = true) {
        putBoolean(KEY_ALARMS_ENABLED, enabled)
    }
}