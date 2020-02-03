package com.maruchin.medihelper.presentation.feature.options.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maruchin.medihelper.domain.model.ChangePasswordErrors
import com.maruchin.medihelper.domain.usecases.user.ChangePasswordUseCase
import com.maruchin.medihelper.domain.utils.ChangePasswordValidator
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val changePasswordUseCase: ChangePasswordUseCase
) : ViewModel() {

    val password = MutableLiveData<String>()
    val passwordConfirm = MutableLiveData<String>()

    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val actionPasswordChanged: LiveData<Boolean>
        get() = _actionPasswordChanged
    val errorPassword: LiveData<String>
        get() = _errorPassword
    val errorPasswordConfirm: LiveData<String>
        get() = _errorPasswordConfirm
    val errorGlobal: LiveData<String>
        get() = _errorGlobal

    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _actionPasswordChanged = ActionLiveData()
    private val _errorPassword = MutableLiveData<String>()
    private val _errorPasswordConfirm = MutableLiveData<String>()
    private val _errorGlobal = MutableLiveData<String>()

    fun changePassword() = viewModelScope.launch {
        _loadingInProgress.postValue(true)

        val params = ChangePasswordUseCase.Params(
            newPassword = password.value,
            newPasswordConfirm = passwordConfirm.value
        )
        val errors = changePasswordUseCase.execute(params)

        _loadingInProgress.postValue(false)
        if (errors.noErrors) {
            _actionPasswordChanged.sendAction()
        } else {
            postErrors(errors)
        }
    }

    private fun postErrors(errors: ChangePasswordErrors) {
        var passwordError = getPasswordError(errors)
        var passwordConfirmError = getPasswordConfirmError(errors)
        if (passwordError == null &&
            passwordConfirmError == null &&
            errors.passwordsNotTheSame
        ) {
            val message = "Hasła nie są takie same"
            passwordError = message
            passwordConfirmError = message
        }

        _errorPassword.postValue(passwordError)
        _errorPasswordConfirm.postValue(passwordConfirmError)

        if (errors.undefinedError) {
            _errorGlobal.postValue("Błąd zmiany hasła")
        }
    }

    private fun getPasswordError(errors: ChangePasswordErrors): String? {
        return when {
            errors.emptyPassword -> "Hasło jest wymagane"
            errors.weakPassword -> "Hasło musi mieć przynajmniej 6 znaków"
            else -> null
        }
    }

    private fun getPasswordConfirmError(errors: ChangePasswordErrors): String? {
        return when {
            errors.emptyPasswordConfirm -> "Potwierdzenie hasła jest wymagane"
            else -> null
        }
    }
}