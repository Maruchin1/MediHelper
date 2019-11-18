package com.example.medihelper.mainapp.options

import androidx.lifecycle.*
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.service.ApiResponse
import com.example.medihelper.service.AppMode
import com.example.medihelper.service.PersonService
import com.example.medihelper.service.ServerApiService
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val personService: PersonService,
    private val serverApiService: ServerApiService
) : ViewModel() {

    val colorPrimary: LiveData<Int> = personService.getMainPersonColorLive()
    val isAppModeOffline: LiveData<Boolean>
    val isAppModeLogged: LiveData<Boolean>
    val isAppModeConnected: LiveData<Boolean>
    val loggedUserEmail: LiveData<String> = serverApiService.getUserEmailLive()
    val connectedProfileName: LiveData<String> = serverApiService.getConnectedProfileNameLive()

    val errorChangePassword: LiveData<String>
        get() = _errorChangePassword
    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress
    val actionLogoutComplete: LiveData<Boolean>
        get() = _actionLogoutComplete
    val actionCancelPatronConnectComplete: LiveData<Boolean>
        get() = _actionCancelPatronConnectComplete

    private val _errorChangePassword = MutableLiveData<String>()
    private val _loadingInProgress = MutableLiveData<Boolean>(false)
    private val _actionLogoutComplete = ActionLiveData()
    private val _actionCancelPatronConnectComplete = ActionLiveData()

    private val appMode: LiveData<AppMode> = serverApiService.getAppModeLive()

    init {
        isAppModeOffline = Transformations.map(appMode) { it == AppMode.OFFLINE }
        isAppModeLogged = Transformations.map(appMode) { it == AppMode.LOGGED }
        isAppModeConnected = Transformations.map(appMode) { it == AppMode.CONNECTED }
    }

    fun changePassword(newPassword: String) = viewModelScope.launch {
        _loadingInProgress.postValue(true)
        val apiResponse = serverApiService.changeUserPassword(newPassword)
        val errorString = mapApiResponseToErrString(apiResponse)
        _errorChangePassword.postValue(errorString)
        _loadingInProgress.postValue(false)
    }

    fun logoutUser(clearLocalData: Boolean) = viewModelScope.launch {
        serverApiService.logoutUser(clearLocalData)
        _actionLogoutComplete.sendAction()
    }

    fun cancelPatronConnect() = viewModelScope.launch {
        serverApiService.cancelPatronConnection()
        _actionCancelPatronConnectComplete.sendAction()
    }

    private fun mapApiResponseToErrString(apiResponse: ApiResponse) = when (apiResponse) {
        ApiResponse.OK -> null
        ApiResponse.TIMEOUT -> "Przekroczono czas połaczenia"
        else -> "Błąd połączenia"
    }
}