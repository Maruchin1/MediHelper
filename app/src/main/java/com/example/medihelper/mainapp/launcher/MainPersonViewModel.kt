package com.example.medihelper.mainapp.launcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.service.InitialDataService
import kotlinx.coroutines.launch

class MainPersonViewModel(
    private val initialDataService: InitialDataService
) : ViewModel() {

    private val _form = FormModel()
    private val _formError = FormErroModel()
    private val _setupEndAction = ActionLiveData()

    val form: FormModel
        get() = _form
    val formError: FormErroModel
        get() = _formError
    val setupEndAction: LiveData<Boolean>
        get() = _setupEndAction

    fun saveMainProfile() = viewModelScope.launch {
        if (isFormValid()) {
            with(initialDataService) {
                createMainPerson(_form.userName.value!!)
                checkInitialData()
            }
            _setupEndAction.sendAction()
        }
    }

    private fun isFormValid(): Boolean {
        val userNameErr = if (_form.userName.value.isNullOrEmpty()) {
            "Twoje imię jest wymagane"
        } else null

        _formError.userNameErr.postValue(userNameErr)

        return userNameErr == null
    }

    class FormModel {
        val userName = MutableLiveData<String>()
    }

    class FormErroModel {
        val userNameErr = MutableLiveData<String>()
    }
}