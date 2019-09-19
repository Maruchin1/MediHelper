package com.example.medihelper.mainapp.more.loggeduser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewPasswordViewModel : ViewModel() {

    val newPasswordLive = MutableLiveData<String>()
    val passwordConfirmationLive = MutableLiveData<String>()
    val errorNewPasswordLive = MutableLiveData<String>()
    val errorPasswordConfirmationLive = MutableLiveData<String>()

    fun validatePasswordsInputData(): Boolean {
        var inputDataValid = true
        if (newPasswordLive.value.isNullOrEmpty()) {
            errorNewPasswordLive.postValue("Nowe hasło jest wymagane")
            inputDataValid = false
        } else {
            errorNewPasswordLive.postValue(null)
        }
        if (passwordConfirmationLive.value.isNullOrEmpty()) {
            errorPasswordConfirmationLive.postValue("Potwierdzenie hasła jest wymagane")
            inputDataValid = false
        } else {
            errorPasswordConfirmationLive.postValue(null)
        }
        if (!newPasswordLive.value.isNullOrEmpty() && !passwordConfirmationLive.value.isNullOrEmpty()) {
            if (newPasswordLive.value != passwordConfirmationLive.value) {
                val errorMessage = "Hasła nie są takie same"
                errorNewPasswordLive.postValue(errorMessage)
                errorPasswordConfirmationLive.postValue(errorMessage)
                inputDataValid = false
            } else {
                errorNewPasswordLive.postValue(null)
                errorPasswordConfirmationLive.postValue(null)
            }
        }
        return inputDataValid
    }
}