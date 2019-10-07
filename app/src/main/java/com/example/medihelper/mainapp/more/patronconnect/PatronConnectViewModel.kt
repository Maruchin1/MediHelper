package com.example.medihelper.mainapp.more.patronconnect

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.*
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.remotedatabase.AuthenticationApi
import com.example.medihelper.remotedatabase.dto.ConnectedPersonDto
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.launch
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import com.example.medihelper.mainapp.MainActivity
import org.koin.android.ext.android.get


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
    val loadingInProgressLive = MutableLiveData<Boolean>()
    val connectSuccessfulLive = MutableLiveData<Pair<String, Int>>()

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
//                val profileDataDto = authenticationApi.patronConnect(connectionKey)
                val connectedPersonDto = ConnectedPersonDto(
                    personName = "Test",
                    personColorResId = R.color.colorPersonBlue,
                    authToken = "adsaasgasg"
                )
                saveConnectedPersonAppMode(connectedPersonDto.authToken)
                mainApplication.switchToConnectedPersonDatabase()
                updateMainPersonData(connectedPersonDto)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            loadingInProgressLive.postValue(false)
        }
    }

    private fun saveConnectedPersonAppMode(authToken: String) = sharedPrefService.run {
        saveAuthToken(authToken)
        deleteUserEmail()
    }

    private suspend fun updateMainPersonData(connectedPersonDto: ConnectedPersonDto) {
        val personRepository: PersonRepository = mainApplication.get()
        personRepository.getMainPersonEntity().let { mainPerson ->
            mainPerson.personName = connectedPersonDto.personName
            mainPerson.personColorResID = connectedPersonDto.personColorResId
            personRepository.update(mainPerson)
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