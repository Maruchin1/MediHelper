package com.example.medihelper.mainapp.more

import androidx.lifecycle.ViewModel
import com.example.medihelper.services.SharedPrefService

class LoggedUserViewModel(private val sharedPrefService: SharedPrefService) : ViewModel() {

    val loggedUserEmailLive = sharedPrefService.getLoggedUserEmailLive()

    fun logoutUser() {
        sharedPrefService.run {
            deleteLoggedUserAuthToken()
            deleteLoggedUserEmail()
        }
    }
}