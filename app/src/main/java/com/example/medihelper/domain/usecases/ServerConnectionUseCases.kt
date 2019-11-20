package com.example.medihelper.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.medihelper.domain.entities.AppMode
import com.example.medihelper.domain.repositories.AppUserRepo

class ServerConnectionUseCases(
    private val appUserRepo: AppUserRepo
) {

    fun getAppMode(): AppMode {
        val authToken = appUserRepo.getAuthToken() ?: ""
        val email = appUserRepo.getUserEmail() ?: ""
        return getAppMode(authToken, email)
    }

    fun getAppModeLive(): LiveData<AppMode> {
        val authTokenLive = appUserRepo.getAuthTokenLive()
        val emailLive = appUserRepo.getUserEmailLive()
        var authToken = ""
        var email = ""
        val appModeLive = MediatorLiveData<AppMode>()
        appModeLive.addSource(authTokenLive) { newAuthToken ->
            authToken = newAuthToken
            appModeLive.postValue(getAppMode(authToken, email))
        }
        appModeLive.addSource(emailLive) { newEmail ->
            email = newEmail
            appModeLive.postValue(getAppMode(authToken, email))
        }
        return appModeLive
    }

    private fun getAppMode(authToken: String, email: String): AppMode {
        return when {
            authToken.isNotEmpty() && email.isNotEmpty() -> AppMode.LOGGED
            authToken.isNotEmpty() && email.isEmpty() -> AppMode.CONNECTED
            else -> AppMode.OFFLINE
        }
    }
}