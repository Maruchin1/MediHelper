package com.maruchin.medihelper.presentation.feature.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maruchin.medihelper.domain.usecases.user.SignOutUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val actionSignOutSuccessful: LiveData<Boolean>
        get() = _actionSignOutSuccessful

    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _actionSignOutSuccessful = ActionLiveData()

    fun signOutUser() = viewModelScope.launch {
        _loadingInProgress.postValue(true)

        signOutUseCase.execute()

        _loadingInProgress.postValue(false)
        _actionSignOutSuccessful.sendAction()
    }
}