package com.example.medihelper.mainapp.launcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.service.ApiResponse
import com.example.medihelper.service.ServerApiService
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val serverApiService: ServerApiService
) : ViewModel() {

    private var _form = FormModel()
    private var _formError = FormErrorModel()
    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _registerError = MutableLiveData<String>()

    val form: FormModel
        get() = _form
    val formError: FormErrorModel
        get() = _formError
    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val registerError: LiveData<String>
        get() = _registerError

    fun registerUser() = viewModelScope.launch {
        if (isFormValid()) {
            _loadingInProgress.postValue(true)
            val apiResponse = serverApiService.registerNewUser(
                email = _form.email.value!!,
                password = _form.password.value!!
            )
            val errorString = mapApiResponseToErrString(apiResponse)
            _registerError.postValue(errorString)
            _loadingInProgress.postValue(false)
        }
    }

    private fun isFormValid(): Boolean {
        val userName = _form.userName.value
        val email = _form.email.value
        val password = _form.password.value
        val passwordConfirm = _form.passwordConfirm.value

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

        _formError.userNameErr.postValue(userNameErr)
        _formError.emailErr.postValue(emailErr)
        _formError.passwordErr.postValue(passwordErr)
        _formError.passwordConfirmErr.postValue(passwordConfirmErr)

        return arrayOf(userNameErr, emailErr, passwordErr, passwordConfirmErr).all { it == null }
    }

    private fun mapApiResponseToErrString(apiResponse: ApiResponse) = when (apiResponse) {
        ApiResponse.OK -> null
        ApiResponse.TIMEOUT -> "Przekroczono czas połączenia"
        ApiResponse.ALREADY_EXISTS -> "Użytkownik o podanym adresie już istnieje"
        else -> "Błąd połączenia"
    }

    class FormModel {
        val userName = MutableLiveData<String>()
        val email = MutableLiveData<String>()
        val password = MutableLiveData<String>()
        val passwordConfirm = MutableLiveData<String>()
    }

    class FormErrorModel {
        val userNameErr = MutableLiveData<String>()
        val emailErr = MutableLiveData<String>()
        val passwordErr = MutableLiveData<String>()
        val passwordConfirmErr = MutableLiveData<String>()
    }
}