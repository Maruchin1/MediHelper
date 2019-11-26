package com.maruchin.medihelper.domain.repositories

import androidx.lifecycle.LiveData
import com.maruchin.medihelper.domain.entities.ApiResponse
import com.maruchin.medihelper.domain.entities.AppMode

interface AppUserRepo {
    fun getAppMode(): AppMode
    fun getAppModeLive(): LiveData<AppMode>
    fun getUserEmailLive(): LiveData<String>
    fun enqueueServerSync()
    suspend fun registerNewUser(userName: String, email: String, password: String): ApiResponse
    suspend fun loginUser(email: String, password: String): Triple<ApiResponse, String?, Boolean?>
    suspend fun connectWithPatron(connectionKey: String): ApiResponse
    suspend fun changeUserPassword(newPassword: String): ApiResponse
    suspend fun logoutUser(clearLocalData: Boolean)
    suspend fun cancelPatronConnection()
}