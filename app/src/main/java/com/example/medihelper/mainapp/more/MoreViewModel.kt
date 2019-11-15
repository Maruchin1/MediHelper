package com.example.medihelper.mainapp.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.medihelper.service.AppMode
import com.example.medihelper.service.PersonService
import com.example.medihelper.service.ServerApiService

class MoreViewModel(
    private val personService: PersonService,
    private val serverApiService: ServerApiService
) : ViewModel() {

    val colorPrimaryLive: LiveData<Int>
    val loggedUserInfoLive: LiveData<String>
    val connectedPersonInfoLive: LiveData<String>

    val isAppModeLogged: Boolean
        get() = serverApiService.getAppMode() == AppMode.LOGGED
    val isAppModeConnected: Boolean
        get() = serverApiService.getAppMode() == AppMode.CONNECTED

    init {
        colorPrimaryLive = personService.getMainPersonColorLive()
        loggedUserInfoLive = Transformations.map(serverApiService.getUserEmailLive()) { email ->
            if (email.isNullOrEmpty()) "Nie zalogowano" else email
        }
        connectedPersonInfoLive = Transformations.map(serverApiService.getAppModeLive()) { appMode ->
            when (appMode) {
                AppMode.CONNECTED -> "Połączono z opiekunem"
                else -> "Nie połączono z opiekunem"
            }
        }
    }
}