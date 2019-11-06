package com.example.medihelper.mainapp.more.loginregister

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.service.ServerApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginRegisterViewModel(
    private val serverApiService: ServerApiService
) : ViewModel() {
    private val TAG = "LoginRegisterViewModel"

    val emailLive = MutableLiveData<String>()
    val passwordLive = MutableLiveData<String>()
    val passwordConfirmationLive = MutableLiveData<String>()
    val errorEmailLive = MutableLiveData<String>()
    val errorPasswordLive = MutableLiveData<String>()
    val errorPasswordConfirmationLive = MutableLiveData<String>()
    val loadingInProgressLive = MutableLiveData<Boolean>()
    val remoteDataIsAvailableAction = ActionLiveData()
    val loginErrorLive = MutableLiveData<String>()
    val registerErrorLive = MutableLiveData<String>()

    fun resetViewModel() {
        listOf(
            emailLive,
            passwordLive,
            passwordConfirmationLive,
            errorEmailLive,
            errorPasswordLive,
            errorPasswordConfirmationLive
        ).forEach { liveData ->
            liveData.postValue(null)
        }
    }

    fun loginUser() = viewModelScope.launch {
        Log.i(TAG, "loginUser")
        if (validateInputData(Mode.LOGIN)) {
            loadingInProgressLive.postValue(true)
            var errorMessage = serverApiService.loginUser(
                email = emailLive.value!!,
                password = passwordLive.value!!
            )
            if (errorMessage != null) {
                loginErrorLive.postValue(errorMessage)
            } else {
                val responsePair = serverApiService.isRemoteDataAvailable()
                val isAvailable = responsePair.first
                errorMessage = responsePair.second
                if (isAvailable == true) {
                    remoteDataIsAvailableAction.sendAction()
                } else {
                    loginErrorLive.postValue(errorMessage)
                }
            }
            loadingInProgressLive.postValue(false)
        }
    }

    fun registerNewUser() = viewModelScope.launch {
        if (validateInputData(Mode.REGISTER)) {
            loadingInProgressLive.postValue(true)
            val errorMessage = serverApiService.registerNewUser(
                email = emailLive.value!!,
                password = passwordLive.value!!
            )
            registerErrorLive.postValue(errorMessage)
            loadingInProgressLive.postValue(false)
        }
    }

    fun useRemoteDataAfterLogin() = GlobalScope.launch {
        serverApiService.useRemoteDataAfterLogin()
        loginErrorLive.postValue(null)
    }

    fun useLocalDataAfterLogin() = viewModelScope.launch {
        loadingInProgressLive.postValue(true)
        val errorMessage = serverApiService.useLocalDataAfterLogin()
        loginErrorLive.postValue(errorMessage)
        loadingInProgressLive.postValue(false)
    }

    private fun validateInputData(viewModelMode: Mode): Boolean {
        var inputDataValid = true
        if (emailLive.value.isNullOrEmpty()) {
            errorEmailLive.postValue("Adres e-mail jest wymagany")
            inputDataValid = false
        } else {
            errorEmailLive.postValue(null)
        }
        if (passwordLive.value.isNullOrEmpty()) {
            errorPasswordLive.postValue("Hasło jest wymagane")
            inputDataValid = false
        } else {
            errorPasswordLive.postValue(null)
        }
        if (viewModelMode == Mode.REGISTER) {
            if (passwordConfirmationLive.value.isNullOrEmpty()) {
                errorPasswordConfirmationLive.postValue("Potwierdzenie hasła jest wymagane")
                inputDataValid = false
            } else {
                errorPasswordConfirmationLive.postValue(null)
            }
            if (!passwordLive.value.isNullOrEmpty() && !passwordConfirmationLive.value.isNullOrEmpty()) {
                if (passwordLive.value != passwordConfirmationLive.value) {
                    val errorMessage = "Hasła nie są takie same"
                    errorPasswordLive.postValue(errorMessage)
                    errorPasswordConfirmationLive.postValue(errorMessage)
                    inputDataValid = false
                } else {
                    errorPasswordLive.postValue(null)
                    errorPasswordConfirmationLive.postValue(null)
                }
            }
        }
        return inputDataValid
    }

    enum class Mode {
        LOGIN, REGISTER
    }
}