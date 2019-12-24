package com.maruchin.medihelper.presentation.feature.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maruchin.medihelper.domain.usecases.user.SignInUseCase
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val actionSignInSuccessful: LiveData<Boolean>
        get() = _actionSignInSuccessful

    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _actionSignInSuccessful = ActionLiveData()

    fun signIn() = viewModelScope.launch {
        _loadingInProgress.postValue(true)

        val email = email.value!!
        val password = password.value!!

        val success = signInUseCase.execute(email, password)

        _loadingInProgress.postValue(false)
        if (success) {
            _actionSignInSuccessful.sendAction()
        }
    }
}