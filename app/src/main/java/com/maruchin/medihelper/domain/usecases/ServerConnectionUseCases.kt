package com.maruchin.medihelper.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.maruchin.medihelper.domain.entities.ApiResponse
import com.maruchin.medihelper.domain.entities.AppMode
import com.maruchin.medihelper.domain.repositories.AppUserRepo
import com.maruchin.medihelper.domain.repositories.PersonRepo

class ServerConnectionUseCases(
    private val appUserRepo: AppUserRepo,
    private val personRepo: PersonRepo
) {
    fun getUserEmailLive() = appUserRepo.getUserEmailLive()

    fun getConnectedProfileNameLive() = Transformations.map(personRepo.getMainLive()) { it?.name }

    fun getAppMode(): AppMode {
        return appUserRepo.getAppMode()
    }

    fun getAppModeLive(): LiveData<AppMode> {
        return appUserRepo.getAppModeLive()
    }

    fun enqueueServerSync() {
        appUserRepo.enqueueServerSync()
    }

    suspend fun registerNewUser(userName: String, email: String, password: String): ApiResponse {
        return appUserRepo.registerNewUser(userName, email, password)
    }

    suspend fun loginUser(email: String, password: String): Triple<ApiResponse, String?, Boolean?> {
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
}