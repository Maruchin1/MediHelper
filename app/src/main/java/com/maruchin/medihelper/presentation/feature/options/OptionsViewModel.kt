package com.maruchin.medihelper.presentation.feature.options

import androidx.lifecycle.*
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.usecases.user.ChangePasswordUseCase
import com.maruchin.medihelper.domain.usecases.user.GetCurrUserUseCase
import com.maruchin.medihelper.domain.usecases.user.SignOutUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val getCurrUserUseCase: GetCurrUserUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    val userEmail: LiveData<String>

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
    }

    fun signOutUser() = viewModelScope.launch {
        _loadingInProgress.postValue(true)

        signOutUseCase.execute()

        _loadingInProgress.postValue(false)
        _actionSignOutSuccessful.sendAction()
    }
}