package com.example.medihelper.mainapp.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.service.AppMode
import com.example.medihelper.service.PersonService
import com.example.medihelper.service.ServerApiService

class OptionsViewModel(
    private val personService: PersonService,
    private val serverApiService: ServerApiService
) : ViewModel() {

    private val appMode: LiveData<AppMode> = serverApiService.getAppModeLive()

    val colorPrimary: LiveData<Int> = personService.getMainPersonColorLive()
    val isAppModeOffline: LiveData<Boolean>
    val isAppModeLogged: LiveData<Boolean>
    val isAppModeConnected: LiveData<Boolean>

    init {
        isAppModeOffline = Transformations.map(appMode) { it == AppMode.OFFLINE }
        isAppModeLogged = Transformations.map(appMode) { it == AppMode.LOGGED }
        isAppModeConnected = Transformations.map(appMode) { it == AppMode.CONNECTED }
    }
}