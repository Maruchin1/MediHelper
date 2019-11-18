package com.example.medihelper.mainapp.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.service.ApiResponse
import com.example.medihelper.service.ServerApiService
import kotlinx.coroutines.launch

class LoginViewModel(
    private val serverApiService: ServerApiService
) : ViewModel() {

    private var _form = FormModel()
    private var _formError = FormErrorModel()
    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _loginError = MutableLiveData<String>()

    val form: FormModel
        get() = _form
    val formError: FormErrorModel
        get() = _formError
    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val loginError: LiveData<String>
        get() = _loginError

    fun loginUser() = viewModelScope.launch {
        if (isFormValid()) {
            _loadingInProgress.postValue(true)
            val apiResponse = serverApiService.initialLoginUser(
                email = _form.email.value!!,
                password = _form.password.value!!
            )
            val errorString = mapApiResponseToErrString(apiResponse)
            _loginError.postValue(errorString)
            _loadingInProgress.postValue(false)
        }
    }

    private fun isFormValid(): Boolean {
        val emailErr = if (_form.email.value.isNullOrEmpty()) {
            "Adred e-mail jest wymagany"
        } else null
        val passwordErr = if (_form.password.value.isNullOrEmpty()) {
            "Hasło jest wymagane"
        } else null

        _formError.emailErr.postValue(emailErr)
        _formError.passwordErr.postValue(passwordErr)

        return arrayOf(emailErr, passwordErr).all { it == null }
    }

    private fun mapApiResponseToErrString(apiResponse: ApiResponse) = when (apiResponse) {
        ApiResponse.OK -> null
        ApiResponse.TIMEOUT -> "Przekroczono czas połączenia"
        ApiResponse.INCORRECT_DATA -> "Niepoprawne hasło"
        ApiResponse.NOT_FOUND -> "Nie znaleziono użytkownika"
        else -> "Błąd połączenia"
    }

    class FormModel {
        val email = MutableLiveData<String>()
        val password = MutableLiveData<String>()
    }

    class FormErrorModel {
        val emailErr = MutableLiveData<String>()
        val passwordErr = MutableLiveData<String>()
    }
}