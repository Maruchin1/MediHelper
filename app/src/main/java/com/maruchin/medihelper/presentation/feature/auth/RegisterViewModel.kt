package com.maruchin.medihelper.presentation.feature.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maruchin.medihelper.domain.model.SignUpErrors
import com.maruchin.medihelper.domain.usecases.profile.CreateMainProfileUseCase
import com.maruchin.medihelper.domain.usecases.user.SignUpUseCase
import com.maruchin.medihelper.domain.utils.SignUpValidator
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get

class RegisterViewModel(
    private val signUpUseCase: SignUpUseCase
) : ViewModel(), KoinComponent {

    val userName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val passwordConfirm = MutableLiveData<String>()

    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val actionUserSignedUp: LiveData<Boolean>
        get() = _actionUserSignedUp
    val errorUserName: LiveData<String>
        get() = _errorUserName
    val errorEmail: LiveData<String>
        get() = _errorEmail
    val errorPassword: LiveData<String>
        get() = _errorPassword
    val errorPasswordConfirm: LiveData<String>
        get() = _errorPasswordConfirm
    val errorGlobal: LiveData<String>
        get() = _errorGlobal

    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _actionUserSignedUp = ActionLiveData()
    private val _errorUserName = MutableLiveData<String>()
    private val _errorEmail = MutableLiveData<String>()
    private val _errorPassword = MutableLiveData<String>()
    private val _errorPasswordConfirm = MutableLiveData<String>()
    private val _errorGlobal = MutableLiveData<String>()

    fun signUpUser() = viewModelScope.launch {
        _loadingInProgress.postValue(true)

        val params = getSignUpParams()
        val errors = signUpUseCase.execute(params)

        if (errors.noErrors) {
            createMainProfile(params.userName!!)
            _actionUserSignedUp.sendAction()
        } else {
            postErrors(errors)
        }
        _loadingInProgress.postValue(false)
    }

    private fun getSignUpParams(): SignUpUseCase.Params {
        return SignUpUseCase.Params(
            email = email.value,
            password = password.value,
            passwordConfirm = passwordConfirm.value,
            userName = userName.value
        )
    }

    private suspend fun createMainProfile(profileName: String) {
        val useCase: CreateMainProfileUseCase = get()
        useCase.execute(profileName)
    }

    private fun postErrors(errors: SignUpErrors) {
        val userNameError = if (errors.emptyUserName) {
            "Imię jest wymagane"
        } else null
        val emailError = if (errors.emptyEmail) {
            "Adres e-mail jest wymagany"
        } else null
        var passwordError = if (errors.emptyPassword) {
            "Hasło jest wymamgane"
        } else null
        var passwordConfirmError = if (errors.emptyPasswordConfirm) {
            "Powtórzenie hasła jest wymagane"
        } else null

        if (passwordError == null && passwordConfirmError == null && errors.passwordsNotTheSame) {
            val message = "Podane hasła nie są takie same"
            passwordError = message
            passwordConfirmError = message
        }

        _errorUserName.postValue(userNameError)
        _errorEmail.postValue(emailError)
        _errorPassword.postValue(passwordError)
        _errorPasswordConfirm.postValue(passwordConfirmError)

        //todo obsłużyć pozostałe błędy
    }
}