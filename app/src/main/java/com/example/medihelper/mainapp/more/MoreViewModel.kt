package com.example.medihelper.mainapp.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.services.SharedPrefService

class MoreViewModel(private val sharedPrefService: SharedPrefService) : ViewModel() {

    val loggedUserInfoLive: LiveData<String>
    private val loggedUserEmailLive = sharedPrefService.getLoggedUserEmailLive()

    init {
        loggedUserInfoLive = Transformations.map(loggedUserEmailLive) { email ->
            if (email.isNullOrEmpty()) "Nie zalogowano" else email
        }
    }
}