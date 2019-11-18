package com.example.medihelper.mainapp.authentication

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.service.ApiResponse
import com.example.medihelper.service.ServerApiService
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.launch


class PatronConnectViewModel(
    private val serverApiService: ServerApiService
) : ViewModel() {

    val keyChar1 = MutableLiveData<String>()
    val keyChar2 = MutableLiveData<String>()
    val keyChar3 = MutableLiveData<String>()
    val keyChar4 = MutableLiveData<String>()
    val keyChar5 = MutableLiveData<String>()
    val keyChar6 = MutableLiveData<String>()

    val errorConnectionKey: LiveData<String>
        get() = _errorConnectionKey
    val errorPatronConnect: LiveData<String>
        get() = _errorPatronConnect
    val loadingInProgress: LiveData<Boolean>
        get() = _loadingInProgress

    private val _errorConnectionKey = MutableLiveData<String>()
    private val _errorPatronConnect = MutableLiveData<String>()
    private val _loadingInProgress = MutableLiveData<Boolean>()

    private val allCharList by lazy {
        listOf(keyChar1, keyChar2, keyChar3, keyChar4, keyChar5, keyChar6)
    }

    fun scanQrCode(fragment: Fragment) {
        IntentIntegrator.forSupportFragment(fragment).apply {
            setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
            setPrompt("Zeskanuj kod z aplikacji opiekuna")
            setOrientationLocked(true)
        }.initiateScan()
    }

    fun setConnectionKey(connectionKey: String) {
        val allCharList = allCharList
        for (i in allCharList.indices) {
            allCharList[i].value = connectionKey[i].toString()
        }
    }

    fun loadProfileData() = viewModelScope.launch {
        if (validateInputData()) {
            _loadingInProgress.postValue(true)
            val connectionKey = StringBuilder().apply {
                allCharList.forEach { append(it.value!!) }
            }.toString()
            val apiResponse = serverApiService.connectWithPatron(connectionKey)
            val errorMessage = mapApiResponseToErrString(apiResponse)
            _errorPatronConnect.postValue(errorMessage)
            _loadingInProgress.postValue(false)
        }
    }

    private fun validateInputData(): Boolean {
        val connectionKeyErr = if (allCharList.any { it.value.isNullOrEmpty() }) {
            "Nie podano kodu opiekuna"
        } else null

        _errorConnectionKey.postValue(connectionKeyErr)

        return connectionKeyErr == null
    }

    private fun mapApiResponseToErrString(apiResponse: ApiResponse) = when (apiResponse) {
        ApiResponse.OK -> null
        ApiResponse.TIMEOUT -> "Przekroczono czas połączenia"
        else -> "Błąd połączenia"
    }
}