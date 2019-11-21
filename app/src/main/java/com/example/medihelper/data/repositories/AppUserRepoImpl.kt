package com.example.medihelper.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.work.*
import com.example.medihelper.data.local.DeletedHistory
import com.example.medihelper.data.local.SharedPref
import com.example.medihelper.data.local.dao.MedicineDao
import com.example.medihelper.data.local.dao.MedicinePlanDao
import com.example.medihelper.data.local.dao.PersonDao
import com.example.medihelper.data.local.dao.PlannedMedicineDao
import com.example.medihelper.data.local.model.PersonEntity
import com.example.medihelper.data.remote.ApiResponseMapper
import com.example.medihelper.data.remote.api.AuthenticationApi
import com.example.medihelper.data.remote.api.RegisteredUserApi
import com.example.medihelper.data.remote.dto.ConnectedPersonDto
import com.example.medihelper.data.remote.dto.NewPasswordDto
import com.example.medihelper.data.remote.dto.UserCredentialsDto
import com.example.medihelper.domain.entities.ApiResponse
import com.example.medihelper.domain.entities.AppMode
import com.example.medihelper.domain.repositories.AppUserRepo
import com.example.medihelper.data.sync.ConnectedPersonSyncWorker
import com.example.medihelper.data.sync.LoggedUserSyncWorker
import com.example.medihelper.data.sync.ServerSyncWorker
import org.koin.core.KoinComponent
import org.koin.core.get

class AppUserRepoImpl(
    private val context: Context,
    private val sharedPref: SharedPref,
    private val authenticationApi: AuthenticationApi,
    private val registeredUserApi: RegisteredUserApi,
    private val apiResponseMapper: ApiResponseMapper,
    private val deletedHistory: DeletedHistory
) : AppUserRepo, KoinComponent {

    private class AuthTokenNotAvailableException : Exception("AuthToken not available")

    override fun getAppMode(): AppMode {
        val authToken = sharedPref.getAuthToken()
        val userEmail = sharedPref.getUserEmail()
        return getAppMode(authToken, userEmail)
    }

    override fun getAppModeLive(): LiveData<AppMode> {
        val authTokenLive = sharedPref.getAuthTokenLive()
        val emailLive = sharedPref.getUserEmailLive()
        var authToken = ""
        var email = ""
        val appModeLive = MediatorLiveData<AppMode>()
        appModeLive.addSource(authTokenLive) { newAuthToken ->
            authToken = newAuthToken
            appModeLive.postValue(getAppMode(authToken, email))
        }
        appModeLive.addSource(emailLive) { newEmail ->
            email = newEmail
            appModeLive.postValue(getAppMode(authToken, email))
        }
        return appModeLive
    }

    override fun getUserEmailLive(): LiveData<String> {
        return sharedPref.getUserEmailLive()
    }

    override fun enqueueServerSync() {
        val syncWorkBuilder = when (getAppMode()) {
            AppMode.LOGGED -> OneTimeWorkRequestBuilder<LoggedUserSyncWorker>()
            AppMode.CONNECTED -> OneTimeWorkRequestBuilder<ConnectedPersonSyncWorker>()
            else -> null
        }
        if (syncWorkBuilder != null) {
            sharedPref.getAuthToken()?.let { authToken ->
                val syncWork = syncWorkBuilder.setConstraints(
                    Constraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                ).setInputData(
                    workDataOf(
                        ServerSyncWorker.KEY_AUTH_TOKEN to authToken
                    )
                ).build()
                WorkManager.getInstance(context).enqueue(syncWork)
            } ?: throw AuthTokenNotAvailableException()
        }
    }

    override suspend fun registerNewUser(email: String, password: String): ApiResponse {
        val userCredentialsDto = UserCredentialsDto(email, password)
        return try {
            authenticationApi.registerNewUser(userCredentialsDto)
            ApiResponse.OK
        } catch (ex: Exception) {
            ex.printStackTrace()
            apiResponseMapper.getError(ex)
        }
    }

    override suspend fun loginUser(email: String, password: String): Pair<ApiResponse, Boolean?> {
        val userCredentialsDto = UserCredentialsDto(email, password)
        return try {
            val loginResponseDto = authenticationApi.loginUser(userCredentialsDto)
            sharedPref.saveAuthToken(loginResponseDto.authToken)
            sharedPref.saveUserEmail(userCredentialsDto.email)
            Pair(ApiResponse.OK, loginResponseDto.isDataAvailable)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Pair(apiResponseMapper.getError(ex), null)
        }
    }

    override suspend fun connectWithPatron(connectionKey: String): ApiResponse {
        return try {
            val connectedPersonDto = authenticationApi.patronConnect(connectionKey)
            sharedPref.saveAuthToken(connectedPersonDto.authToken)
            sharedPref.deleteUserEmail()
            switchToConnectedDatabase()
            initConnectedPersonDatabase(connectedPersonDto)
            enqueueServerSync()
            ApiResponse.OK
        } catch (ex: Exception) {
            ex.printStackTrace()
            apiResponseMapper.getError(ex)
        }
    }

    override suspend fun changeUserPassword(newPassword: String): ApiResponse {
        return sharedPref.getAuthToken()?.let { authToken ->
            try {
                val newPasswordDto = NewPasswordDto(value = newPassword)
                registeredUserApi.changeUserPassword(authToken, newPasswordDto)
                ApiResponse.OK
            } catch (ex: Exception) {
                ex.printStackTrace()
                apiResponseMapper.getError(ex)
            }
        } ?: throw AuthTokenNotAvailableException()
    }

    override suspend fun logoutUser(clearLocalData: Boolean) {
        val medicineDao: MedicineDao = get()
        val personDao: PersonDao = get()
        val medicinePlanDao: MedicinePlanDao = get()
        val plannedMedicineDao: PlannedMedicineDao = get()
        sharedPref.deleteAuthToken()
        sharedPref.deleteUserEmail()
        deletedHistory.clearAllHitory()
        if (clearLocalData) {
            personDao.deleteAll()
            medicineDao.deleteAll()
        } else {
            with(personDao) {
                val entityList = getAllList()
                entityList.forEach {
                    it.personSynchronized = false
                }
                update(entityList)
            }
            with(medicineDao) {
                val entityList = getAllList()
                entityList.forEach {
                    it.medicineSynchronized = false
                }
                update(entityList)
            }
            with(medicinePlanDao) {
                val entityList = getAllList()
                entityList.forEach {
                    it.medicinePlanSynchronized = false
                }
                update(entityList)
            }
            with(plannedMedicineDao) {
                val entityList = getAllList()
                entityList.forEach {
                    it.plannedMedicineSynchronized = false
                }
                update(entityList)
            }
        }
    }

    override suspend fun cancelPatronConnection() {
        val medicineDao: MedicineDao = get()
        val personDao: PersonDao = get()
        sharedPref.deleteAuthToken()
        medicineDao.deleteAll()
        personDao.deleteAll()
        switchToMainDatabase()
    }

    private fun switchToConnectedDatabase() {
        //todo dopisać przełączanie
//        unloadKoinModules(
//            listOf(
//                viewModelModule,
//                serviceModule,
//                remoteDataModule,
//                localDataModule,
//                mainRoomModule
//            )
//        )
//        loadKoinModules(
//            listOf(
//                connectedRoomModule,
//                localDataModule,
//                remoteDataModule,
//                serviceModule,
//                viewModelModule
//            )
//        )
    }

    private fun switchToMainDatabase() {
        //todo dopisać przełączanie
//        unloadKoinModules(
//            listOf(
//                viewModelModule,
//                serviceModule,
//                remoteDataModule,
//                localDataModule,
//                connectedRoomModule
//            )
//        )
//        loadKoinModules(
//            listOf(
//                mainRoomModule,
//                localDataModule,
//                remoteDataModule,
//                serviceModule,
//                viewModelModule
//            )
//        )
    }

    private suspend fun initConnectedPersonDatabase(connectedPersonDto: ConnectedPersonDto) {
        val mainPerson = PersonEntity(
            personId = 0,
            personRemoteId = connectedPersonDto.personRemoteId,
            personName = connectedPersonDto.personName,
            personColorResId = connectedPersonDto.personColorResId,
            mainPerson = true,
            connectionKey = null,
            personSynchronized = true
        )
        val newPersonDao: PersonDao = get()
        newPersonDao.insert(mainPerson)
    }

    private fun getAppMode(authToken: String?, userEmail: String?): AppMode {
        return when {
            !authToken.isNullOrEmpty() && !userEmail.isNullOrEmpty() -> AppMode.LOGGED
            !authToken.isNullOrEmpty() && !userEmail.isNullOrEmpty() -> AppMode.CONNECTED
            else -> AppMode.OFFLINE
        }
    }
}