package com.example.medihelper.mainapp.more.loggeduser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medihelper.R
import com.example.medihelper.custom.ActionLiveData
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.example.medihelper.localdatabase.repositories.MedicineRepository
import com.example.medihelper.localdatabase.repositories.PersonRepository
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import com.example.medihelper.remotedatabase.api.RegisteredUserApi
import com.example.medihelper.remotedatabase.dto.NewPasswordDto
import com.example.medihelper.services.SharedPrefService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class LoggedUserViewModel(
    private val sharedPrefService: SharedPrefService,
    private val registeredUserApi: RegisteredUserApi,
    private val medicineRepository: MedicineRepository,
    private val personRepository: PersonRepository,
    private val medicinePlanRepository: MedicinePlanRepository,
    private val plannedMedicineRepository: PlannedMedicineRepository
) : ViewModel() {

    val loggedUserEmailLive = sharedPrefService.getLoggedUserEmailLive()

    val loadingStartedAction = ActionLiveData()
    val changePasswordErrorLive = MutableLiveData<Int>()

    fun logoutUser() = GlobalScope.launch {
        sharedPrefService.run {
            deleteLoggedUserAuthToken()
            deleteLoggedUserEmail()
        }
        listOf(
            medicineRepository,
            personRepository,
            medicinePlanRepository,
            plannedMedicineRepository
        ).forEach {
            it.clearDeletedRemoteIDList()
            it.resetSynchronizationData()
        }
    }

    fun changeUserPassword(newPassword: String) = viewModelScope.launch {
        val authToken = sharedPrefService.getLoggedUserAuthToken()
        if (authToken != null) {
            loadingStartedAction.sendAction()
            try {
                registeredUserApi.changeUserPassword(authToken, NewPasswordDto(value = newPassword))
                changePasswordErrorLive.postValue(null)
            } catch (e: Exception) {
                e.printStackTrace()
                changePasswordErrorLive.postValue(getErrorMessage(e))
            }
        }
    }

    private fun getErrorMessage(e: Exception) = when(e) {
        is SocketTimeoutException -> R.string.error_timeout
        is HttpException -> when (e.code()) {
            401 -> R.string.error_authorization
            else -> R.string.error_connection
        }
        else -> R.string.error_connection
    }
}