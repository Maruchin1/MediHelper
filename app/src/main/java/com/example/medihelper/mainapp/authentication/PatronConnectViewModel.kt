package com.example.medihelper.mainapp.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.service.ApiResponse
import com.example.medihelper.service.ServerApiService
import kotlinx.coroutines.launch


class PatronConnectViewModel(
    private val serverApiService: ServerApiService
) : ViewModel() {

    private var _formModel = FormModel()
    private val _connectionKeyError = MutableLiveData<String>()
    private val _patronConnectError = MutableLiveData<String>()
    private val _loadingInProgress = MutableLiveData<Boolean>()

    val formModel: FormModel
        get() = _formModel
    val connectionKeyError: LiveData<String>
        get() = _connectionKeyError
    val patronConnectError: LiveData<String>
        get() = _patronConnectError
    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress

    fun setConnectionKey(connectionKey: String) {
        val allCharList = _formModel.allCharList
        for (i in allCharList.indices) {
            allCharList[i].value = connectionKey[i].toString()
        }
    }

    fun loadProfileData() = viewModelScope.launch {
        if (validateInputData()) {
            _loadingInProgress.postValue(true)
            val connectionKey = StringBuilder().apply {
                _formModel.allCharList.forEach { append(it.value!!) }
            }.toString()
            val apiResponse = serverApiService.connectWithPatron(connectionKey)
            val errorMessage = mapApiResponseToErrString(apiResponse)
            _patronConnectError.postValue(errorMessage)
            _loadingInProgress.postValue(false)
        }
    }

    private fun validateInputData(): Boolean {
        val connectionKeyErr = if (_formModel.allCharList.any { it.value.isNullOrEmpty() }) {
            "Nie podano kodu opiekuna"
        } else null

        _connectionKeyError.postValue(connectionKeyErr)

        return connectionKeyErr == null
    }

    private fun mapApiResponseToErrString(apiResponse: ApiResponse) = when (apiResponse) {
        ApiResponse.OK -> null
        ApiResponse.TIMEOUT -> "Przekroczono czas połączenia"
        else -> "Błąd połączenia"
    }

    class FormModel {
        val keyChar1Live = MutableLiveData<String>()
        val keyChar2Live = MutableLiveData<String>()
        val keyChar3Live = MutableLiveData<String>()
        val keyChar4Live = MutableLiveData<String>()
        val keyChar5Live = MutableLiveData<String>()
        val keyChar6Live = MutableLiveData<String>()
        val allCharList by lazy {
            listOf(keyChar1Live, keyChar2Live, keyChar3Live, keyChar4Live, keyChar5Live, keyChar6Live)
        }
    }
}