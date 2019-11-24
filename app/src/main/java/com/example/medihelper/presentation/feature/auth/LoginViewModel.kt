package com.example.medihelper.presentation.feature.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.domain.entities.ApiResponse
import com.example.medihelper.domain.usecases.PersonUseCases
import com.example.medihelper.domain.usecases.ServerConnectionUseCases
import kotlinx.coroutines.launch

class LoginViewModel(
    private val serverConnectionUseCases: ServerConnectionUseCases,
    private val personUseCases: PersonUseCases
) : ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    val errorEmail: LiveData<String>
        get() = _errorEmail
    val errorPassword: LiveData<String>
        get() = _errorPassword
    val errorLogin: LiveData<String>
        get() = _errorLogin
    val loadingInProcess: LiveData<Boolean>
        get() = _loadingInProgress

    private val _errorEmail = MutableLiveData<String>()
    private val _errorPassword = MutableLiveData<String>()
    private val _errorLogin = MutableLiveData<String>()
    private val _loadingInProgress = MutableLiveData<Boolean>(false)


    fun loginUser() = viewModelScope.launch {
        if (isFormValid()) {
            _loadingInProgress.postValue(true)
            val returnedTriple = serverConnectionUseCases.loginUser(
                email = email.value!!,
                password = password.value!!
            )
            val apiResponse = returnedTriple.first
            val userName = returnedTriple.second
            val isRemoteDataAvailable = returnedTriple.third
            //todo osbłużyć kiedy jest konflikt danych z serwerem

            val errorString = mapApiResponseToErrString(apiResponse)
            if (errorString == null) {
                if (userName != null) {
                    if (personUseCases.mainPersonExists()) {
                        personUseCases.updateMainPerson(userName)
                    } else {
                        personUseCases.addMainPerson(userName)
                    }
                }
                serverConnectionUseCases.enqueueServerSync()
            }

            _errorLogin.postValue(errorString)
            _loadingInProgress.postValue(false)
        }
    }

    private fun isFormValid(): Boolean {
        val email = email.value
        val password = password.value

        val emailError = if (email.isNullOrEmpty()) {
            "Adred e-mail jest wymagany"
        } else null
        val passwordError = if (password.isNullOrEmpty()) {
            "Hasło jest wymagane"
        } else null

        _errorEmail.postValue(emailError)
        _errorPassword.postValue(passwordError)

        return arrayOf(emailError, passwordError).all { it == null }
    }

    private fun mapApiResponseToErrString(apiResponse: ApiResponse) = when (apiResponse) {
        ApiResponse.OK -> null
        ApiResponse.TIMEOUT -> "Przekroczono czas połączenia"
        ApiResponse.INCORRECT_DATA -> "Niepoprawne hasło"
        ApiResponse.NOT_FOUND -> "Nie znaleziono użytkownika"
        else -> "Błąd połączenia"
    }
}