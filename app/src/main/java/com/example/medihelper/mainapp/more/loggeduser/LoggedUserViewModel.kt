package com.example.medihelper.mainapp.more.loggeduser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.R
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.remotedatabase.api.RegisteredUserApi
import com.example.medihelper.remotedatabase.dto.NewPasswordDto
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class LoggedUserViewModel(
    private val sharedPrefService: SharedPrefService,
    private val registeredUserApi: RegisteredUserApi
) : ViewModel() {

    val loggedUserEmailLive = sharedPrefService.getLoggedUserEmailLive()

    val loadingStartedAction = ActionLiveData<Nothing>()
    val changePasswordErrorAction = ActionLiveData<Int>()

    fun logoutUser() {
        sharedPrefService.run {
            deleteLoggedUserAuthToken()
            deleteLoggedUserEmail()
        }
    }

    fun changeUserPassword(newPassword: String) = viewModelScope.launch {
        val authToken = sharedPrefService.getLoggedUserAuthToken()
        if (authToken != null) {
            loadingStartedAction.sendAction()
            try {
                registeredUserApi.changeUserPassword(authToken, NewPasswordDto(value = newPassword))
                changePasswordErrorAction.sendAction()
            } catch (e: Exception) {
                e.printStackTrace()
                changePasswordErrorAction.sendAction(getErrorMessage(e))
            }
        }
    }

    private fun getErrorMessage(e: Exception) = when(e) {
        is SocketTimeoutException -> R.string.error_timeout
        is HttpException -> when (e.code()) {
            401 -> R.string.error_authorization
            else -> R.string.error_connection
        }
        else -> R.string.error_connection
    }
}