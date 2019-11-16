package com.example.medihelper.mainapp.more.patronconnect

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.service.ApiResponse
import com.example.medihelper.service.ServerApiService
import kotlinx.coroutines.launch


class PatronConnectViewModel(
    private val serverApiService: ServerApiService
) : ViewModel() {
    private val TAG = "PatronConnectViewModel"

    val keyChar1Live = MutableLiveData<String>()
    val keyChar2Live = MutableLiveData<String>()
    val keyChar3Live = MutableLiveData<String>()
    val keyChar4Live = MutableLiveData<String>()
    val keyChar5Live = MutableLiveData<String>()
    val keyChar6Live = MutableLiveData<String>()
    val errorConnectionKeyLive = MutableLiveData<String>()
    val patronConnectErrorLive = MutableLiveData<String>()
    val loadingInProgressLive = MutableLiveData<Boolean>()

    private val charLiveList by lazy {
        listOf(keyChar1Live, keyChar2Live, keyChar3Live, keyChar4Live, keyChar5Live, keyChar6Live)
    }

    fun setConnectionKey(connectionKey: String) {
        for (i in charLiveList.indices) {
            charLiveList[i].value = connectionKey[i].toString()
        }
    }

    fun loadProfileData() = viewModelScope.launch {
        if (validateInputData()) {
            loadingInProgressLive.postValue(true)
            val connectionKey = StringBuilder().apply {
                charLiveList.forEach { append(it.value!!) }
            }.toString()
            val apiResponse = serverApiService.connectWithPatron(connectionKey)
            val errorMessage = when (apiResponse) {
                ApiResponse.OK -> null
                ApiResponse.TIMEOUT -> "Przekroczono czas połączenia"
                else -> "Błąd połączenia"
            }
            patronConnectErrorLive.postValue(errorMessage)
            loadingInProgressLive.postValue(false)
        }
    }

    private fun validateInputData(): Boolean {
        var valid = true
        if (charLiveList.any { it.value.isNullOrEmpty() }) {
            errorConnectionKeyLive.postValue("Nie podano kodu opiekuna")
            valid = false
        } else {
            errorConnectionKeyLive.postValue(null)
        }
        return valid
    }
}