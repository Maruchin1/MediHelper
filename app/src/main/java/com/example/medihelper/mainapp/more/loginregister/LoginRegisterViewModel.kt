package com.example.medihelper.mainapp.more.loginregister

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import com.example.medihelper.remotedatabase.ApiResponse
import com.example.medihelper.remotedatabase.api.AuthenticationApi
import com.example.medihelper.remotedatabase.api.RegisteredUserApi
import com.example.medihelper.remotedatabase.dto.UserCredentialsDto
import com.example.medihelper.services.WorkerService
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginRegisterViewModel(
    private val sharedPrefService: SharedPrefService,
    private val authenticationApi: AuthenticationApi,
    private val registeredUserApi: RegisteredUserApi,
    private val workerService: WorkerService,
    private val medicineRepository: MedicineRepository,
    private val personRepository: PersonRepository,
    private val medicinePlanRepository: MedicinePlanRepository,
    private val plannedMedicineRepository: PlannedMedicineRepository
) : ViewModel() {
    private val TAG = "LoginRegisterViewModel"

    val emailLive = MutableLiveData<String>()
    val passwordLive = MutableLiveData<String>()
    val passwordConfirmationLive = MutableLiveData<String>()
    val errorEmailLive = MutableLiveData<String>()
    val errorPasswordLive = MutableLiveData<String>()
    val errorPasswordConfirmationLive = MutableLiveData<String>()
    val loadingStartedAction = ActionLiveData<Boolean>()
    val isRemoteDataAvailableAction = ActionLiveData<Boolean>()
    val loginResponseAction = ActionLiveData<ApiResponse>()
    val registrationResponseAction = ActionLiveData<ApiResponse>()

    private var tempAuthToken = ""
    private var tempUserEmail = ""

    fun resetViewModel() {
        listOf(
            emailLive,
            passwordLive,
            passwordConfirmationLive,
            errorEmailLive,
            errorPasswordLive,
            errorPasswordConfirmationLive
        ).forEach { liveData ->
            liveData.postValue(null)
        }
    }

    fun loginUser() = viewModelScope.launch {
        if (validateInputData(Mode.LOGIN)) {
            loadingStartedAction.sendAction(true)
            val userCredentialsDto = UserCredentialsDto(
                email = emailLive.value!!,
                password = passwordLive.value!!
            )
            try {
                tempAuthToken = authenticationApi.loginUser(userCredentialsDto)
                tempUserEmail = userCredentialsDto.email
                val isRemoteDataAvailable = registeredUserApi.isDataAvailable(tempAuthToken)
                if (isRemoteDataAvailable) {
                    isRemoteDataAvailableAction.sendAction(true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //todo coś zrobić z tą absługą błędów
            loginResponseAction.sendAction(ApiResponse.OK)
        }
    }

    fun registerNewUser() = viewModelScope.launch {
        if (validateInputData(Mode.REGISTER)) {
            loadingStartedAction.sendAction(true)
            val userCredentials = UserCredentialsDto(
                email = emailLive.value!!,
                password = passwordLive.value!!
            )
            val response = try {
                authenticationApi.registerNewUser(userCredentials)
                ApiResponse.OK
            } catch (e: Exception) {
                e.printStackTrace()
                ApiResponse.getResponseByException(e)
            }
            registrationResponseAction.sendAction(response)
        }
    }

    fun useRemoteDataAfterLogin() = GlobalScope.launch {
        listOf(
            medicineRepository,
            personRepository,
            medicinePlanRepository,
            plannedMedicineRepository
        ).forEach {
            it.deleteAll()
        }
        saveLoginData()
        workerService.enqueueSynchronizeData()
    }

    fun useLocalDataAfterLogin() = viewModelScope.launch {
        try {
            registeredUserApi.deleteAllData(tempAuthToken)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        saveLoginData()
        workerService.enqueueSynchronizeData()
    }

    private fun saveLoginData() = sharedPrefService.run {
        saveLoggedUserAuthToken(tempAuthToken)
        saveLoggedUserEmail(tempUserEmail)
    }

    private fun validateInputData(viewModelMode: Mode): Boolean {
        var inputDataValid = true
        if (emailLive.value.isNullOrEmpty()) {
            errorEmailLive.postValue("Adres e-mail jest wymagany")
            inputDataValid = false
        } else {
            errorEmailLive.postValue(null)
        }
        if (passwordLive.value.isNullOrEmpty()) {
            errorPasswordLive.postValue("Hasło jest wymagane")
            inputDataValid = false
        } else {
            errorPasswordLive.postValue(null)
        }
        if (viewModelMode == Mode.REGISTER) {
            if (passwordConfirmationLive.value.isNullOrEmpty()) {
                errorPasswordConfirmationLive.postValue("Potwierdzenie hasła jest wymagane")
                inputDataValid = false
            } else {
                errorPasswordConfirmationLive.postValue(null)
            }
            if (!passwordLive.value.isNullOrEmpty() && !passwordConfirmationLive.value.isNullOrEmpty()) {
                if (passwordLive.value != passwordConfirmationLive.value) {
                    val errorMessage = "Hasła nie są takie same"
                    errorPasswordLive.postValue(errorMessage)
                    errorPasswordConfirmationLive.postValue(errorMessage)
                    inputDataValid = false
                } else {
                    errorPasswordLive.postValue(null)
                    errorPasswordConfirmationLive.postValue(null)
                }
            }
        }
        return inputDataValid
    }

    enum class Mode {
        LOGIN, REGISTER
    }
}