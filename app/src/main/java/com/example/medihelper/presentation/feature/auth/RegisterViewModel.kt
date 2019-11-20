package com.example.medihelper.presentation.feature.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.domain.entities.ApiResponse
import com.example.medihelper.domain.usecases.ServerConnectionUseCases
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val serverConnectionUseCases: ServerConnectionUseCases
) : ViewModel() {

    val userName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val passwordConfirm = MutableLiveData<String>()

    val errorUserName: LiveData<String>
        get() = _errorUserName
    val errorEmail: LiveData<String>
        get() = _errorEmail
    val errorPassword: LiveData<String>
        get() = _errorPassword
    val errorPasswordConfirm: LiveData<String>
        get() = _errorPasswordConfirm
    val errorRegister: LiveData<String>
        get() = _errorRegister
    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress

    private val _errorUserName = MutableLiveData<String>()
    private val _errorEmail = MutableLiveData<String>()
    private val _errorPassword = MutableLiveData<String>()
    private val _errorPasswordConfirm = MutableLiveData<String>()
    private val _errorRegister = MutableLiveData<String>()
    private val _loadingInProgress = MutableLiveData<Boolean>(false)

    fun registerUser() = viewModelScope.launch {
        if (isFormValid()) {
            _loadingInProgress.postValue(true)
            val apiResponse = serverConnectionUseCases.registerNewUser(
                email = email.value!!,
                password = password.value!!
            )
            val errorString = mapApiResponseToErrString(apiResponse)
            _errorRegister.postValue(errorString)
            _loadingInProgress.postValue(false)
        }
    }

    private fun isFormValid(): Boolean {
        val userName = userName.value
        val email = email.value
        val password = password.value
        val passwordConfirm = passwordConfirm.value

        val userNameErr = if (userName.isNullOrEmpty()) {
            "Twoje imię jest wymagane"
        } else null
        val emailErr = if (email.isNullOrEmpty()) {
            "Adres e-mail jest wymagany"
        } else null
        var passwordErr = if (password.isNullOrEmpty()) {
            "Hasło jest wymagane"
        } else null
        var passwordConfirmErr = if (passwordConfirm.isNullOrEmpty()) {
            "Potwierdzenie hasła jest wymagane"
        } else null

        if (passwordErr == null && passwordConfirmErr == null) {
            if (password != passwordConfirm) {
                val errorMessage = "Hasła nie są takie same"
                passwordErr = errorMessage
                passwordConfirmErr = errorMessage
            }
        }

        _errorUserName.postValue(userNameErr)
        _errorEmail.postValue(emailErr)
        _errorPassword.postValue(passwordErr)
        _errorPasswordConfirm.postValue(passwordConfirmErr)

        return arrayOf(userNameErr, emailErr, passwordErr, passwordConfirmErr).all { it == null }
    }

    private fun mapApiResponseToErrString(apiResponse: ApiResponse) = when (apiResponse) {
        ApiResponse.OK -> null
        ApiResponse.TIMEOUT -> "Przekroczono czas połączenia"
        ApiResponse.ALREADY_EXISTS -> "Użytkownik o podanym adresie już istnieje"
        else -> "Błąd połączenia"
    }
}