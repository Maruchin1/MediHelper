package com.maruchin.medihelper.presentation.feature.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _actionPasswordChanged = ActionLiveData()
    private val _errorPassword = MutableLiveData<String>()
    private val _errorPasswordConfirm = MutableLiveData<String>()

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

    private fun postErrors(errors: ChangePasswordValidator.Errors) {
        var passwordError = if (errors.emptyPassword) {
            "Hasło jest wymagane"
        } else null
        var passwordConfirmError = if (errors.emptyPasswordConfirm) {
            "Potwierdzenie hasła jest wymagane"
        } else null
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
    }
}