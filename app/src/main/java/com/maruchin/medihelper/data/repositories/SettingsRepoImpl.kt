package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.data.utils.SharedPref
import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.repositories.SettingsRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SettingsRepoImpl(
    private val sharedPref: SharedPref
) : SettingsRepo {

    override suspend fun setReminderMode(mode: ReminderMode) = withContext(Dispatchers.IO) {
        sharedPref.saveReminderMode(mode)
        return@withContext
    }

    override suspend fun getReminderMode(): ReminderMode = withContext(Dispatchers.IO) {
        return@withContext sharedPref.getReminderMode()
    }

    override suspend fun getLiveReminderMode(): LiveData<ReminderMode> = withContext(Dispatchers.IO) {
        return@withContext sharedPref.getLiveReminderMode()
    }


}