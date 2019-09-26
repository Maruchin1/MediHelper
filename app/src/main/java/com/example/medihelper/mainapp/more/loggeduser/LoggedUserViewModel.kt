package com.example.medihelper.mainapp.more.loggeduser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.remotedatabase.ApiResponse
import com.example.medihelper.remotedatabase.api.RegisteredUserApi
import com.example.medihelper.remotedatabase.dto.NewPasswordDto
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.launch

class LoggedUserViewModel(
    private val sharedPrefService: SharedPrefService,
    private val registeredUserApi: RegisteredUserApi
) : ViewModel() {

    val loggedUserEmailLive = sharedPrefService.getLoggedUserEmailLive()

    val loadingStartedAction = ActionLiveData<Boolean>()
    val changePasswordResponseAction = ActionLiveData<ApiResponse>()

    fun logoutUser() {
        sharedPrefService.run {
            deleteLoggedUserAuthToken()
            deleteLoggedUserEmail()
        }
    }

    fun changeUserPassword(newPassword: String) = viewModelScope.launch {
        val authToken = sharedPrefService.getLoggedUserAuthToken()
        if (authToken != null) {
            loadingStartedAction.postValue(true)
            val response = try {
                registeredUserApi.changeUserPassword(authToken, NewPasswordDto(value = newPassword))
                ApiResponse.OK
            } catch (e: Exception) {
                ApiResponse.getResponseByException(e)
            }
            changePasswordResponseAction.sendAction(response)
        }
    }


}