package com.example.medihelper.mainapp.more.loggeduser

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.remotedatabase.RegisteredUserRemoteRepository
import com.example.medihelper.remotedatabase.pojos.NewPasswordDto
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class LoggedUserViewModel(
    private val sharedPrefService: SharedPrefService,
    private val registeredUserRemoteRepository: RegisteredUserRemoteRepository
) : ViewModel() {

    val loggedUserEmailLive = sharedPrefService.getLoggedUserEmailLive()

    val loadingStartedAction = ActionLiveData<Boolean>()
    val changePasswordSuccessfulAction = ActionLiveData<Boolean>()

    fun logoutUser() {
        sharedPrefService.run {
            deleteLoggedUserAuthToken()
            deleteLoggedUserEmail()
        }
    }

    fun changeUserPassword(context: Context, newPassword: String) = viewModelScope.launch {
        val authToken = sharedPrefService.getLoggedUserAuthToken()
        if (authToken != null) {
            loadingStartedAction.postValue(true)
            var changePasswordSuccessful = false
            try {
                registeredUserRemoteRepository.changeUserPassword(authToken, NewPasswordDto(value = newPassword))
                changePasswordSuccessful = true
            } catch (e: SocketTimeoutException) {
                Toast.makeText(context, "Przekroczono czas oczekiwania", Toast.LENGTH_LONG).show()
            } catch (e: HttpException) {
                Toast.makeText(context, "Error ${e.code()}, ${e.message()}", Toast.LENGTH_LONG).show()
            }
            changePasswordSuccessfulAction.postValue(changePasswordSuccessful)
        }
    }


}