package com.example.medihelper.mainapp.launcher

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.service.InitialDataService
import kotlinx.coroutines.launch

class MainPersonViewModel(
    private val initialDataService: InitialDataService
) : ViewModel() {

    val userNameLive = MutableLiveData<String>()
    val errorUserNameLive = MutableLiveData<String>()
    val actionInitialSetupEnd = ActionLiveData()

    fun saveMainProfile() {
        if (isFormValid()) {
            viewModelScope.launch {
                with(initialDataService) {
                    createMainPerson(userNameLive.value!!)
                    checkInitialData()
                }
                actionInitialSetupEnd.sendAction()
            }
        }
    }

    private fun isFormValid(): Boolean {
        var isValid = true

        errorUserNameLive.value = if (userNameLive.value.isNullOrEmpty()) {
            isValid = false
            "Podanie imienia jest wymagane"
        } else null

        return isValid
    }
}