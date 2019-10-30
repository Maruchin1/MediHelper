package com.example.medihelper.mainapp.more.patronconnect

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medihelper.*
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.localdatabase.entity.PersonEntity
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.remotedatabase.AuthenticationApi
import com.example.medihelper.remotedatabase.dto.ConnectedPersonDto
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import retrofit2.HttpException
import java.net.SocketTimeoutException


class PatronConnectViewModel(
    application: Application,
    private val authenticationApi: AuthenticationApi,
    private val sharedPrefService: SharedPrefService
) : AndroidViewModel(application) {
    private val TAG = "PatronConnectViewModel"

    val keyChar1Live = MutableLiveData<String>()
    val keyChar2Live = MutableLiveData<String>()
    val keyChar3Live = MutableLiveData<String>()
    val keyChar4Live = MutableLiveData<String>()
    val keyChar5Live = MutableLiveData<String>()
    val keyChar6Live = MutableLiveData<String>()
    val errorConnectionKeyLive = MutableLiveData<String>()
    val patronConnectErrorLive = MutableLiveData<Int>()
    val loadingInProgressLive = MutableLiveData<Boolean>()
    val patronConnectSuccessfulAction = ActionLiveData()

    private val mainApplication by lazy { application as MainApplication }
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
                val connectedPersonDto = authenticationApi.patronConnect(connectionKey)
                saveConnectedPersonAppMode(connectedPersonDto.authToken)
                mainApplication.switchToConnectedPersonDatabase()
                initConnectedPersonDatabase(connectedPersonDto)
                patronConnectSuccessfulAction.sendAction()
            } catch (e: Exception) {
                e.printStackTrace()
                patronConnectErrorLive.postValue(getErrorMessage(e))
            }
            loadingInProgressLive.postValue(false)
        }
    }

    private fun saveConnectedPersonAppMode(authToken: String) = sharedPrefService.run {
        saveAuthToken(authToken)
        deleteUserEmail()
    }

    private suspend fun initConnectedPersonDatabase(connectedPersonDto: ConnectedPersonDto) {
        val personRepository: PersonRepository = mainApplication.get()
        val mainPerson = PersonEntity(
            personRemoteID = connectedPersonDto.personRemoteId,
            personName = connectedPersonDto.personName,
            personColorResID = connectedPersonDto.personColorResId,
            mainPerson = true
        )
        personRepository.insert(mainPerson)
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