package com.example.medihelper.mainapp.more.loginregister

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.R
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import com.example.medihelper.remotedatabase.api.AuthenticationApi
import com.example.medihelper.remotedatabase.api.RegisteredUserApi
import com.example.medihelper.remotedatabase.dto.UserCredentialsDto
import com.example.medihelper.services.WorkerService
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

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
    val loadingStartedAction = ActionLiveData<Nothing>()
    val remoteDataIsAvailableAction = ActionLiveData<Nothing>()
    val loginErrorAction = ActionLiveData<Int>()
    val registerErrorAction = ActionLiveData<Int>()

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
            loadingStartedAction.sendAction()
            val userCredentialsDto = UserCredentialsDto(
                email = emailLive.value!!,
                password = passwordLive.value!!
            )
            try {
                tempAuthToken = authenticationApi.loginUser(userCredentialsDto)
                tempUserEmail = userCredentialsDto.email
                val isRemoteDataAvailable = registeredUserApi.isDataAvailable(tempAuthToken)
                if (isRemoteDataAvailable) {
                    remoteDataIsAvailableAction.sendAction()
                } else {
                    saveLoginDataAndSync()
                    loginErrorAction.sendAction()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                loginErrorAction.sendAction(getErrorMessage(e))
            }
        }
    }

    fun registerNewUser() = viewModelScope.launch {
        if (validateInputData(Mode.REGISTER)) {
            loadingStartedAction.sendAction()
            val userCredentials = UserCredentialsDto(
                email = emailLive.value!!,
                password = passwordLive.value!!
            )
            try {
                authenticationApi.registerNewUser(userCredentials)
                registerErrorAction.sendAction()
            } catch (e: Exception) {
                e.printStackTrace()
                registerErrorAction.sendAction(getErrorMessage(e))
            }
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
        saveLoginDataAndSync()
    }

    fun useLocalDataAfterLogin() = viewModelScope.launch {
        loadingStartedAction.sendAction()
        try {
            registeredUserApi.deleteAllData(tempAuthToken)
            loginErrorAction.sendAction()
        } catch (e: Exception) {
            e.printStackTrace()
            loginErrorAction.sendAction(getErrorMessage(e))
        }
        saveLoginDataAndSync()
    }

    private fun saveLoginDataAndSync() {
        sharedPrefService.run {
            saveLoggedUserAuthToken(tempAuthToken)
            saveLoggedUserEmail(tempUserEmail)
        }
        workerService.enqueueSynchronizeData()
    }

    private fun getErrorMessage(e: Exception) = when (e) {
        is SocketTimeoutException -> R.string.error_timeout
        is HttpException -> when (e.code()) {
                422 -> R.string.error_incorrect_credentials
                409 -> R.string.error_user_exists
                404 -> R.string.error_user_not_found
                else -> R.string.error_connection

        }
        else -> R.string.error_connection
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