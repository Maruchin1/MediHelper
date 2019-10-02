package com.example.medihelper.mainapp.more.patronconnect

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.remotedatabase.PersonApi
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.launch

class PatronConnectViewModel(
    private val personApi: PersonApi,
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
    val connectSuccessfulLive = MutableLiveData<Pair<String, Int>>()

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
                val profileDataDto = personApi.getAuthToken(connectionKey)
                Log.i(TAG, "authToken = $profileDataDto")
                val updatedMainPerson = personRepository.getMainPersonEntity().apply {
                    personColorResID = profileDataDto.personColorResId
                }
                personRepository.run {
                    update(updatedMainPerson)
                    deleteAll()
                }
                sharedPrefService.run {
                    saveLoggedUserAuthToken(profileDataDto.authToken)
                    deleteLoggedUserEmail()
                }
                connectSuccessfulLive.postValue(Pair(profileDataDto.personName, profileDataDto.personColorResId))
            } catch (e: Exception) {
                e.printStackTrace()
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
}