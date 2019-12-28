package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.data.utils.SharedPref
import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.repositories.SettingsRepo

class SettingsRepoImpl(
    private val sharedPref: SharedPref
) : SettingsRepo {

    override fun setReminderMode(mode: ReminderMode) {
        sharedPref.saveReminderMode(mode)
    }

    override fun getReminderMode(): ReminderMode {
        return sharedPref.getReminderMode()
    }

    override fun getLiveReminderMode(): LiveData<ReminderMode> {
        return sharedPref.getLiveReminderMode()
    }


}