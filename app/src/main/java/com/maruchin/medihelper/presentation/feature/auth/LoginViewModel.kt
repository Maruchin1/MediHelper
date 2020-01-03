package com.maruchin.medihelper.presentation.feature.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maruchin.medihelper.domain.model.SignInErrors
import com.maruchin.medihelper.domain.usecases.user.SignInUseCase
import com.maruchin.medihelper.domain.utils.SignInValidator
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
        get() = _actionSignInSuccess
    val errorEmail: LiveData<String>
        get() = _errorEmail
    val errorPassword: LiveData<String>
        get() = _errorPassword
    val errorGlobal: LiveData<String>
        get() = _errorGlobal

    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _actionSignInSuccess = ActionLiveData()
    private val _errorEmail = MutableLiveData<String>()
    private val _errorPassword = MutableLiveData<String>()
    private val _errorGlobal = MutableLiveData<String>()

    fun signIn() = viewModelScope.launch {
        _loadingInProgress.postValue(true)

        val params = getSignInParams()
        val errors = signInUseCase.execute(params)

        _loadingInProgress.postValue(false)
        if (errors.noErrors) {
            _actionSignInSuccess.sendAction()
        } else {
            postErrors(errors)
        }
    }

    private fun getSignInParams(): SignInUseCase.Params {
        return SignInUseCase.Params(
            email = email.value,
            password = password.value
        )
    }

    private fun postErrors(errors: SignInErrors) {
        val emailError = getEmailError(errors)
        val passwordError = getPasswordError(errors)

        _errorEmail.postValue(emailError)
        _errorPassword.postValue(passwordError)

        if (errors.undefinedError) {
            _errorGlobal.postValue("Błąd logowania")
        }
    }

    private fun getEmailError(errors: SignInErrors): String? {
        return when {
            errors.emptyEmail -> "Nie podano adresu e-mail"
            errors.incorrectEmail -> "Brak użytkownika o podanym adresie"
            else -> null
        }
    }

    private fun getPasswordError(errors: SignInErrors): String? {
        return when {
            errors.emptyPassword -> "Nie podano hasła"
            errors.incorrectPassword -> "Hasło niepoprawne"
            else -> null
        }
    }
}