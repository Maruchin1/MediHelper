package com.example.medihelper.mainapp.more.loggeduser

import androidx.lifecycle.*
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.service.ServerApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class LoggedUserViewModel(
    private val serverApiService: ServerApiService
) : ViewModel() {

    val loggedUserEmailLive = serverApiService.getUserEmailLive()
    val lastSyncTimeStringLive: LiveData<String>

    val loadingStartedAction = ActionLiveData()
    val changePasswordErrorLive = MutableLiveData<String>()

    init {
        lastSyncTimeStringLive = Transformations.map(serverApiService.getLastSyncTimeLive()) { dateTime ->
            dateTime?.let { SimpleDateFormat.getDateTimeInstance().format(it) }
        }
    }

    fun logoutUser() = GlobalScope.launch {
        serverApiService.logoutUser()
    }

    fun changeUserPassword(newPassword: String) = viewModelScope.launch {
        loadingStartedAction.sendAction()
        val apiResponse = serverApiService.changeUserPassword(newPassword)
        //todo mapować response na tekst
        changePasswordErrorLive.postValue("error")
    }
}