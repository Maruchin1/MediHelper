package com.maruchin.medihelper.data.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.maruchin.medihelper.data.framework.SharedPrefLiveData
import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.repositories.SettingsRepo

class SettingsRepoImpl(
    private val context: Context
) : SettingsRepo {

    companion object {
        private const val PREF_NAME = "settings-shared-pref"
        private const val KEY_REMINDERS_ENABLED = "key-reminders-enabled"
        private const val KEY_REMINDER_MODE = "key-reminder-mode"
    }

    private val pref: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    override suspend fun areRemindersEnabled(): Boolean {
        return pref.getBoolean(KEY_REMINDERS_ENABLED, true)
    }

    override suspend fun areLiveRemindersEnabled(): LiveData<Boolean> {
        return SharedPrefLiveData(pref, KEY_REMINDERS_ENABLED, defaultValue = true)
    }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        pref.edit(commit = true) {
            putBoolean(KEY_REMINDERS_ENABLED, enabled)
        }
    }

    override suspend fun getReminderMode(): ReminderMode {
        val value = pref.getString(KEY_REMINDER_MODE, null)
        return if (value != null) {
            ReminderMode.valueOf(value)
        } else {
            ReminderMode.NOTIFICATIONS
        }
    }

    override suspend fun getLiveReminderMode(): LiveData<ReminderMode> {
        val valueLive = SharedPrefLiveData(pref, KEY_REMINDER_MODE, ReminderMode.NOTIFICATIONS.toString())
        return Transformations.map(valueLive) { value ->
            ReminderMode.valueOf(value)
        }
    }

    override suspend fun setReminderMode(newMode: ReminderMode) {
        pref.edit(commit = true) {
            putString(KEY_REMINDER_MODE, newMode.toString())
        }
    }
}