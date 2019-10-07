package com.example.medihelper.mainapp.more.patronconnect

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.R
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.remotedatabase.AuthenticationApi
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class PatronConnectViewModel(
    private val authenticationApi: AuthenticationApi,
    private val personRepository: PersonRepository,
    private val sharedPrefService: SharedPrefService
) : ViewModel() {
    private val TAG = "PatronConnectViewModel"

    val keyChar1Live = MutableLiveData<String>()
    val keyChar2Live = MutableLiveData<String>()
    val keyChar3Live = MutableLiveData<String>()
    val keyChar4Live = MutableLiveData<String>()
    val keyChar5Live = MutableLiveData<String>()
    val keyChar6Live = MutableLiveData<String>()
    val errorConnectionKeyLive = MutableLiveData<String>()
    val loadingInProgressLive = MutableLiveData<Boolean>()
    val connectSuccessfulAction = ActionLiveData()
    val patronConnectErrorLive = MutableLiveData<Int>()

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
            try {
                val profileDataDto = authenticationApi.patronConnect(connectionKey)
                Log.i(TAG, "authToken = $profileDataDto")
                val updatedMainPerson = personRepository.getMainPersonEntity().apply {
                    personColorResID = profileDataDto.personColorResId
                }
                personRepository.run {
                    update(updatedMainPerson)
                    deleteAll()
                }
                sharedPrefService.run {
                    saveAuthToken(profileDataDto.authToken)
                    deleteUserEmail()
                }
                connectSuccessfulAction.sendAction()
            } catch (e: Exception) {
                e.printStackTrace()
                patronConnectErrorLive.postValue(getErrorMessage(e))
            }
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

    private fun getErrorMessage(e: Exception) = when (e) {
        is SocketTimeoutException -> R.string.error_timeout
        is HttpException -> when (e.code()) {
            404 -> R.string.error_person_not_found
            else -> R.string.error_connection
        }
        else -> R.string.error_connection
    }
}