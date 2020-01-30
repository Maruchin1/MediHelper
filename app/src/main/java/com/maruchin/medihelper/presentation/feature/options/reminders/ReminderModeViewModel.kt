package com.maruchin.medihelper.presentation.feature.options.reminders

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.usecases.settings.GetReminderModeUseCase
import com.maruchin.medihelper.domain.usecases.settings.SetReminderModeUseCase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ReminderModeViewModel(
    private val getReminderModeUseCase: GetReminderModeUseCase,
    private val setReminderModeUseCase: SetReminderModeUseCase
) : ViewModel() {

    val reminderMode: LiveData<ReminderMode>

    init {
        reminderMode = loadReminderMode()
    }

    fun setReminderMode(newMode: ReminderMode) = GlobalScope.launch {
        val modeChanged = modeChanged(newMode)
        if (modeChanged) {
            setReminderModeUseCase.execute(newMode)
        }
    }

    private fun loadReminderMode() = liveData {
        val value = getReminderModeUseCase.execute()
        emit(value)
    }

    private fun modeChanged(newMode: ReminderMode): Boolean {
        val currMode = reminderMode.value
        return currMode != null && currMode != newMode
    }
}