package com.example.medihelper.domain.repositories

import androidx.lifecycle.LiveData

interface AppUserRepo {
    fun getAuthToken(): String?
    fun getUserEmail(): String?
    fun getAuthTokenLive(): LiveData<String>
    fun getUserEmailLive(): LiveData<String>
}