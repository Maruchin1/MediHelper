package com.maruchin.medihelper.presentation.feature.options

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.usecases.settings.AreLiveNotificationsEnabledUseCase
import com.maruchin.medihelper.domain.usecases.settings.SetNotificationsEnabledUseCase
import com.maruchin.medihelper.domain.usecases.user.GetCurrUserEmailUseCase
import com.maruchin.medihelper.domain.usecases.user.SignOutUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val getCurrUserEmailUseCase: GetCurrUserEmailUseCase,
    private val areLiveNotificationsEnabledUseCase: AreLiveNotificationsEnabledUseCase,
    private val setNotificationsEnabledUseCase: SetNotificationsEnabledUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val notificationsHelp: NotificationsHelp,
    private val alarmsHelp: AlarmsHelp
) : ViewModel() {

    val userEmail: LiveData<String>
    val areNotificationEnabled: LiveData<Boolean>
    val areAlarmsEnabled: LiveData<Boolean>

    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val actionSignOutSuccessful: LiveData<Boolean>
        get() = _actionSignOutSuccessful

    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _actionSignOutSuccessful = ActionLiveData()

    init {
        userEmail = getCurrUserEmail()
        areNotificationEnabled = getLiveAreNotificationsEnabled()
        areAlarmsEnabled = getLiveAreAlarmsEnabled()
    }

    fun signOutUser() = viewModelScope.launch {
        _loadingInProgress.postValue(true)
        signOutUseCase.execute()
        _loadingInProgress.postValue(false)
        _actionSignOutSuccessful.sendAction()
    }

    fun setNotificationsEnabled(enabled: Boolean) = viewModelScope.launch {
        setNotificationsEnabledUseCase.execute(enabled)
    }

    fun setAlarmsEnabled(enabled: Boolean) = viewModelScope.launch {
        //todo execute appropriate UseCase to set value
    }

    fun getNotificationsHelp(): List<HelpItemData> {
        return notificationsHelp.generate()
    }

    fun getAlarmsHelp(): List<HelpItemData> {
        return alarmsHelp.generate()
    }

    private fun getCurrUserEmail() = liveData {
        val value = getCurrUserEmailUseCase.execute()
        emit(value)
    }

    private fun getLiveAreNotificationsEnabled() = liveData {
        val source = areLiveNotificationsEnabledUseCase.execute()
        emitSource(source)
    }

    private fun getLiveAreAlarmsEnabled() = liveData {
        //todo execute appropriate UseCase and emit value
        emit(false)
    }
}