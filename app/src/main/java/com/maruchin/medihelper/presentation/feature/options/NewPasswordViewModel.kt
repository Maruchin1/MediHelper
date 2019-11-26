package com.maruchin.medihelper.presentation.feature.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewPasswordViewModel : ViewModel() {

    private val _errorNewPassword = MutableLiveData<String>()
    private val _errorNewPasswordConfirm = MutableLiveData<String>()

    val errorNewPassword: LiveData<String>
        get() = _errorNewPassword
    val errorNewPasswordConfirm: LiveData<String>
        get() = _errorNewPasswordConfirm
    val newPassword = MutableLiveData<String>()
    val newPasswordConfirm = MutableLiveData<String>()


    fun getValidNewPassword() = if (isFormValid()) newPassword.value else null

    private fun isFormValid(): Boolean {
        val newPassword = newPassword.value
        val newPasswordConfirm = newPasswordConfirm.value

        var newPasswordError = if (newPassword.isNullOrEmpty()) {
            "Nowe hasło jest wymagane"
        } else null
        var newPasswordConfirmError = if (newPasswordConfirm.isNullOrEmpty()) {
            "Potwierdzenie hasła jest wymagane"
        } else null
        if (newPasswordError == null && newPasswordConfirmError == null) {
            val errorMessage = if (newPassword != newPasswordConfirm) {
                "Hasła nie są takie same"
            } else null
            newPasswordError = errorMessage
            newPasswordConfirmError = errorMessage
        }

        _errorNewPassword.postValue(newPasswordError)
        _errorNewPasswordConfirm.postValue(newPasswordConfirmError)

        return arrayOf(newPasswordError, newPasswordConfirmError).all { it == null }
    }
}