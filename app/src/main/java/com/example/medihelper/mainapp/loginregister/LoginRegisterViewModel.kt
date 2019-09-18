package com.example.medihelper.mainapp.loginregister

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.remotedatabase.RegisteredUserRemoteRepository
import com.example.medihelper.remotedatabase.pojos.UserCredentialsDto
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class LoginRegisterViewModel(
    private val registeredUserRemoteRepository: RegisteredUserRemoteRepository,
    private val sharedPrefService: SharedPrefService
) : ViewModel() {
    private val TAG = "LoginRegisterViewModel"

    val emailLive = MutableLiveData<String>()
    val passwordLive = MutableLiveData<String>()
    val passwordConfirmationLive = MutableLiveData<String>()
    val errorEmailLive = MutableLiveData<String>()
    val errorPasswordLive = MutableLiveData<String>()
    val errorPasswordConfirmationLive = MutableLiveData<String>()
    val loginSuccessfulAction = ActionLiveData<Boolean>()
    val registrationSuccessfulAction = ActionLiveData<Boolean>()
    val loadingStartedAction = ActionLiveData<Boolean>()

    fun loginUser(context: Context) = viewModelScope.launch {
        Log.i(TAG, "loginUser")
        if (validateInputData(Mode.LOGIN)) {
            loadingStartedAction.postValue(true)
            val userCredentials = UserCredentialsDto(
                email = emailLive.value!!,
                password = passwordLive.value!!
            )
            var authToken: String? = null
            try {
                authToken = registeredUserRemoteRepository.loginUser(userCredentials)
            } catch (e: SocketTimeoutException) {
                Toast.makeText(context, "Przekroczono czas oczekiwania", Toast.LENGTH_LONG).show()
            }
            if (authToken != null) {
                Log.i(TAG, "authToken = $authToken")
                sharedPrefService.saveLoggedUserAuthToken(authToken)
                loginSuccessfulAction.postValue(true)
            } else {
                loginSuccessfulAction.postValue(false)
            }
        }
    }

    fun registerNewUser(context: Context) = viewModelScope.launch {
        if (validateInputData(Mode.REGISTER)) {
            loadingStartedAction.postValue(true)
            val userCredentials = UserCredentialsDto(
                email = emailLive.value!!,
                password = passwordLive.value!!
            )
            try {
                registeredUserRemoteRepository.registerNewUser(userCredentials)
                registrationSuccessfulAction.sendAction(true)
            } catch (e: SocketTimeoutException) {
                Toast.makeText(context, "Przekroczono czas oczekiwania", Toast.LENGTH_LONG).show()
                registrationSuccessfulAction.sendAction(false)
            }
        }
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
                    val errorMessage = "Hasła nie są takie saem"
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