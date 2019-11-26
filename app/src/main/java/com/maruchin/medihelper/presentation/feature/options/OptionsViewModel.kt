package com.maruchin.medihelper.presentation.feature.options

import androidx.lifecycle.*
import com.maruchin.medihelper.presentation.framework.ActionLiveData
import com.maruchin.medihelper.domain.entities.ApiResponse
import com.maruchin.medihelper.domain.entities.AppMode
import com.maruchin.medihelper.domain.usecases.PersonUseCases
import com.maruchin.medihelper.domain.usecases.ServerConnectionUseCases
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val personUseCases: PersonUseCases,
    private val serverConnectionUseCases: ServerConnectionUseCases
) : ViewModel() {

    val colorPrimary: LiveData<Int> = personUseCases.getMainPersonColorLive()
    val appModeOffline: LiveData<Boolean>
    val appModeLogged: LiveData<Boolean>
    val appModeConnected: LiveData<Boolean>
    val loggedUserEmail: LiveData<String> = serverConnectionUseCases.getUserEmailLive()
    val connectedProfileName: LiveData<String?> = serverConnectionUseCases.getConnectedProfileNameLive()

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

    private val appMode: LiveData<AppMode> = serverConnectionUseCases.getAppModeLive()

    init {
        appModeOffline = Transformations.map(appMode) { it == AppMode.OFFLINE }
        appModeLogged = Transformations.map(appMode) { it == AppMode.LOGGED }
        appModeConnected = Transformations.map(appMode) { it == AppMode.CONNECTED }
    }

    fun changePassword(newPassword: String) = viewModelScope.launch {
        _loadingInProgress.postValue(true)
        val apiResponse = serverConnectionUseCases.changeUserPassword(newPassword)
        val errorString = mapApiResponseToErrString(apiResponse)
        _errorChangePassword.postValue(errorString)
        _loadingInProgress.postValue(false)
    }

    fun logoutUser(clearLocalData: Boolean) = viewModelScope.launch {
        serverConnectionUseCases.logoutUser(clearLocalData)
        _actionLogoutComplete.sendAction()
    }

    fun cancelPatronConnect() = viewModelScope.launch {
        serverConnectionUseCases.cancelPatronConnection()
        _actionCancelPatronConnectComplete.sendAction()
    }

    private fun mapApiResponseToErrString(apiResponse: ApiResponse) = when (apiResponse) {
        ApiResponse.OK -> null
        ApiResponse.TIMEOUT -> "Przekroczono czas połaczenia"
        else -> "Błąd połączenia"
    }
}