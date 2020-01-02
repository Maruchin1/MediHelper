package com.maruchin.medihelper.presentation.feature.options

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.ReminderMode
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.usecases.plannedmedicines.ChangePlannedMedicineReminderModeUseCase
import com.maruchin.medihelper.domain.usecases.plannedmedicines.GetLivePlannedMedicineReminderModeUseCase
import com.maruchin.medihelper.domain.usecases.user.GetCurrUserUseCase
import com.maruchin.medihelper.domain.usecases.user.SignOutUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val getCurrUserUseCase: GetCurrUserUseCase,
    private val getLivePlannedMedicineReminderModeUseCase: GetLivePlannedMedicineReminderModeUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val changePlannedMedicineReminderModeUseCase: ChangePlannedMedicineReminderModeUseCase
) : ViewModel() {

    val userEmail: LiveData<String>
    val reminderMode: LiveData<ReminderMode>

    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val actionSignOutSuccessful: LiveData<Boolean>
        get() = _actionSignOutSuccessful

    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _actionSignOutSuccessful = ActionLiveData()

    private val currUser: LiveData<User>

    init {
        currUser = liveData {
            val value = getCurrUserUseCase.execute()
            emit(value)
        }
        userEmail = Transformations.map(currUser) { it.email }
        reminderMode = liveData {
            val source = getLivePlannedMedicineReminderModeUseCase.execute()
            emitSource(source)
        }
    }

    fun signOutUser() = viewModelScope.launch {
        _loadingInProgress.postValue(true)

        signOutUseCase.execute()

        _loadingInProgress.postValue(false)
        _actionSignOutSuccessful.sendAction()
    }

    fun setReminderMode(newMode: ReminderMode) = viewModelScope.launch {
        changePlannedMedicineReminderModeUseCase.execute(newMode)
    }
}