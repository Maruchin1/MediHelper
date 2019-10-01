package com.example.medihelper.mainapp.more.patronconnect

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.remotedatabase.PersonApi
import kotlinx.coroutines.launch

class PatronConnectViewModel(private val personApi: PersonApi) : ViewModel() {
    private val TAG = "PatronConnectViewModel"

    val personTempKeyLive = MutableLiveData<String>()
    val errorPersonTempKey = MutableLiveData<String>()

    fun loadAuthToken() = viewModelScope.launch {
        if (validateInputData()) {
            try {
                val authToken = personApi.getAuthToken(personTempKeyLive.value!!)
                Log.i(TAG, "authToken = $authToken")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun validateInputData(): Boolean {
        var valid = true
        if (personTempKeyLive.value.isNullOrEmpty()) {
            errorPersonTempKey.postValue("Nie podano kodu")
            valid = false
        } else {
            errorPersonTempKey.postValue(null)
        }
        return valid
    }
}