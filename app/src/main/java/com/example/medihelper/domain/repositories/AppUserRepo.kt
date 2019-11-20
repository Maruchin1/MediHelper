package com.example.medihelper.domain.repositories

import androidx.lifecycle.LiveData
import com.example.medihelper.domain.entities.ApiResponse

interface AppUserRepo {
    fun getAuthToken(): String?
    fun getUserEmail(): String?
    fun getAuthTokenLive(): LiveData<String>
    fun getUserEmailLive(): LiveData<String>
    suspend fun registerNewUser(email: String, password: String): ApiResponse
    suspend fun loginUser(email: String, password: String): ApiResponse
    suspend fun connectWithPatron(connectionKey: String): ApiResponse
    suspend fun changeUserPassword(newPassword: String): ApiResponse
    suspend fun logoutUser(clearLocalData: Boolean)
    suspend fun cancelPatronConnection()
}