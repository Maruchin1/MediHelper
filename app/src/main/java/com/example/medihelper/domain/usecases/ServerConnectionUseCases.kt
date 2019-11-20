package com.example.medihelper.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.domain.entities.ApiResponse
import com.example.medihelper.domain.entities.AppMode
import com.example.medihelper.domain.repositories.AppUserRepo
import com.example.medihelper.domain.repositories.PersonRepo

class ServerConnectionUseCases(
    private val appUserRepo: AppUserRepo,
    private val personRepo: PersonRepo
) {
    fun getUserEmailLive() = appUserRepo.getUserEmailLive()

    fun getConnectedProfileNameLive() = Transformations.map(personRepo.getMainLive()) { it.name }

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

    suspend fun registerNewUser(email: String, password: String): ApiResponse {
        return appUserRepo.registerNewUser(email, password)
    }

    suspend fun loginUser(email: String, password: String): ApiResponse {
        return appUserRepo.loginUser(email, password)
    }

    suspend fun connectWithPatron(connectionKey: String): ApiResponse {
        if (getAppMode() == AppMode.LOGGED) {
            logoutUser(clearLocalData = false)
        }
        return appUserRepo.connectWithPatron(connectionKey)
    }

    suspend fun changeUserPassword(newPassword: String): ApiResponse {
        return appUserRepo.changeUserPassword(newPassword)
    }

    suspend fun logoutUser(clearLocalData: Boolean) {
        appUserRepo.logoutUser(clearLocalData)
    }

    suspend fun cancelPatronConnection() {
        appUserRepo.cancelPatronConnection()
    }

    private fun getAppMode(authToken: String, email: String): AppMode {
        return when {
            authToken.isNotEmpty() && email.isNotEmpty() -> AppMode.LOGGED
            authToken.isNotEmpty() && email.isEmpty() -> AppMode.CONNECTED
            else -> AppMode.OFFLINE
        }
    }
}