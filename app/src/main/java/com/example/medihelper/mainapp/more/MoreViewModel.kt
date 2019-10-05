package com.example.medihelper.mainapp.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.services.SharedPrefService

class MoreViewModel(private val sharedPrefService: SharedPrefService) : ViewModel() {

    val loggedUserInfoLive: LiveData<String>
    val isAppModeLogged: Boolean
        get() = sharedPrefService.getAppMode() == SharedPrefService.AppMode.LOGGED
    val isAppModeConnected: Boolean
        get() = sharedPrefService.getAppMode() == SharedPrefService.AppMode.CONNECTED

    init {
        loggedUserInfoLive = Transformations.map(sharedPrefService.getUserEmailLive()) { email ->
            if (email.isNullOrEmpty()) "Nie zalogowano" else email
        }
    }
}