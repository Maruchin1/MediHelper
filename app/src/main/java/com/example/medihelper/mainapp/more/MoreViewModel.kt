package com.example.medihelper.mainapp.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.services.SharedPrefService

class MoreViewModel(
    private val sharedPrefService: SharedPrefService,
    private val personRepository: PersonRepository
) : ViewModel() {

    val colorPrimaryLive: LiveData<Int>
    val loggedUserInfoLive: LiveData<String>
    val connectedPersonInfoLive: LiveData<String>

    val isAppModeLogged: Boolean
        get() = sharedPrefService.getAppMode() == SharedPrefService.AppMode.LOGGED
    val isAppModeConnected: Boolean
        get() = sharedPrefService.getAppMode() == SharedPrefService.AppMode.CONNECTED
    private val mainPersonItemLive = personRepository.getMainPersonItemLive()

    init {
        colorPrimaryLive = Transformations.map(mainPersonItemLive) { it?.personColorResID }
        loggedUserInfoLive = Transformations.map(sharedPrefService.getUserEmailLive()) { email ->
            if (email.isNullOrEmpty()) "Nie zalogowano" else email
        }
        connectedPersonInfoLive = Transformations.map(sharedPrefService.getAppModeLive()) { appMode ->
            when (appMode) {
                SharedPrefService.AppMode.CONNECTED -> "Połączono z opiekunem"
                else -> "Nie połączono z opiekunem"
            }
        }
    }
}