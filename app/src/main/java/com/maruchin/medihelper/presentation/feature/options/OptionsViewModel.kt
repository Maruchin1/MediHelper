package com.maruchin.medihelper.presentation.feature.options

import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.usecases.settings.AreLiveRemindersEnabledUseCase
import com.maruchin.medihelper.domain.usecases.settings.GetLiveReminderModeUseCase
import com.maruchin.medihelper.domain.usecases.settings.SetRemindersEnabledUseCase
import com.maruchin.medihelper.domain.usecases.user.GetCurrUserEmailUseCase
import com.maruchin.medihelper.domain.usecases.user.SignOutUseCase
import com.maruchin.medihelper.presentation.feature.options.reminders.HelpItemData
import com.maruchin.medihelper.presentation.feature.options.reminders.RemindersHelp
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val getCurrUserEmailUseCase: GetCurrUserEmailUseCase,
    private val areLiveRemindersEnabledUseCase: AreLiveRemindersEnabledUseCase,
    private val setRemindersEnabledUseCase: SetRemindersEnabledUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getLiveReminderModeUseCase: GetLiveReminderModeUseCase,
    private val remindersHelp: RemindersHelp
) : ViewModel() {

    val userEmail: LiveData<String>
    val areRemindersEnabled: LiveData<Boolean>
    val remindersEnabledText: LiveData<String>
    val reminderMode: LiveData<String>

    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val actionSignOutSuccessful: LiveData<Boolean>
        get() = _actionSignOutSuccessful

    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _actionSignOutSuccessful = ActionLiveData()

    init {
        userEmail = getCurrUserEmail()
        areRemindersEnabled = getLiveAreRemindersEnabled()
        remindersEnabledText = getLiveRemindersEnabledText()
        reminderMode = getLiveReminderModeText()
    }

    fun onClickShareApp(context: Context) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Polecam  aplikację MediHelper do zarządzania domową apteczką. <tu będzie link>")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun signOutUser() = viewModelScope.launch {
        _loadingInProgress.postValue(true)
        signOutUseCase.execute()
        _loadingInProgress.postValue(false)
        _actionSignOutSuccessful.sendAction()
    }

    fun setRemindersEnabled(enabled: Boolean) = viewModelScope.launch {
        setRemindersEnabledUseCase.execute(enabled)
    }

    fun getRemindersHelp(): List<HelpItemData> {
        return remindersHelp.generate()
    }

    private fun getCurrUserEmail() = liveData {
        val value = getCurrUserEmailUseCase.execute()
        emit(value)
    }

    private fun getLiveAreRemindersEnabled() = liveData {
        val source = areLiveRemindersEnabledUseCase.execute()
        emitSource(source)
    }

    private fun getLiveRemindersEnabledText(): LiveData<String> {
        return Transformations.map(areRemindersEnabled) { enabled ->
            if (enabled) {
                "Przypominanie włączone"
            } else {
                "Przypominanie wyłączone"
            }
        }
    }

    private fun getLiveReminderModeText(): LiveData<String> = liveData {
        val source = getLiveReminderModeUseCase.execute()
        val textLive = Transformations.map(source) { mode ->
            getReminderModeText(mode)
        }
        emitSource(textLive)
    }

    private fun getReminderModeText(mode: ReminderMode): String {
        return when (mode) {
            ReminderMode.NOTIFICATIONS -> "Powiadomienia"
            ReminderMode.ALARMS -> "Alarmy z dźwiękiem"
        }
    }
}