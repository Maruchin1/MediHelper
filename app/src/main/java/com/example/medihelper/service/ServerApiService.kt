package com.example.medihelper.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.work.*
import com.example.medihelper.*
import com.example.medihelper.R
import com.example.medihelper.localdata.AppSharedPref
import com.example.medihelper.localdata.DeletedHistory
import com.example.medihelper.localdata.dao.MedicineDao
import com.example.medihelper.localdata.dao.MedicinePlanDao
import com.example.medihelper.localdata.dao.PersonDao
import com.example.medihelper.localdata.dao.PlannedMedicineDao
import com.example.medihelper.localdata.entity.PersonEntity
import com.example.medihelper.remotedata.api.AuthenticationApi
import com.example.medihelper.remotedata.api.RegisteredUserApi
import com.example.medihelper.remotedata.dto.ConnectedPersonDto
import com.example.medihelper.remotedata.dto.NewPasswordDto
import com.example.medihelper.remotedata.dto.UserCredentialsDto
import com.example.medihelper.serversync.ConnectedPersonSyncWorker
import com.example.medihelper.serversync.LoggedUserSyncWorker
import com.example.medihelper.serversync.ServerSyncWorker
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.get
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.*
import kotlin.Exception

interface ServerApiService {
    fun getAppMode(): AppMode
    fun getAppModeLive(): LiveData<AppMode>
    fun getUserEmailLive(): LiveData<String>
    fun getLastSyncTimeLive(): LiveData<Date>
    fun updateLastSyncTime()
    fun enqueueServerSync()
    suspend fun registerNewUser(email: String, password: String): ApiResponse
    suspend fun loginUser(email: String, password: String): Pair<Boolean?, ApiResponse>
    suspend fun initialLoginUser(email: String, password: String): ApiResponse
    suspend fun useRemoteDataAfterLogin()
    suspend fun useLocalDataAfterLogin(): ApiResponse
    suspend fun changeUserPassword(newPassword: String): ApiResponse
    suspend fun connectWithPatron(connectionKey: String): ApiResponse
    suspend fun logoutUser()
    suspend fun cancelPatronConnection()
}

enum class AppMode {
    OFFLINE, LOGGED, CONNECTED
}

enum class ApiResponse {
    OK, TIMEOUT, NOT_FOUND, ALREADY_EXISTS, INCORRECT_DATA, ERROR
}

class ServerApiServiceImpl(
    private val sharedPref: AppSharedPref,
    private val authenticationApi: AuthenticationApi,
    private val registeredUserApi: RegisteredUserApi,
    private val personDao: PersonDao,
    private val medicineDao: MedicineDao,
    private val medicinePlanDao: MedicinePlanDao,
    private val plannedMedicineDao: PlannedMedicineDao,
    private val workManager: WorkManager,
    private val deletedHistory: DeletedHistory,
    private val initialDataService: InitialDataService
) : ServerApiService, KoinComponent {

    override fun getAppMode(): AppMode {
        val authToken = sharedPref.getAuthToken() ?: ""
        val email = sharedPref.getUserEmail() ?: ""
        return getAppMode(authToken, email)
    }

    override suspend fun registerNewUser(email: String, password: String): ApiResponse {
        val userCredentialsDto = UserCredentialsDto(email, password)
        return try {
            authenticationApi.registerNewUser(userCredentialsDto)
            ApiResponse.OK
        } catch (e: Exception) {
            e.printStackTrace()
            getError(e)
        }
    }

    override suspend fun loginUser(email: String, password: String): Pair<Boolean?, ApiResponse> {
        val userCredentialsDto = UserCredentialsDto(email, password)
        return try {
            val loginResponseDto = authenticationApi.loginUser(userCredentialsDto)
            sharedPref.saveAuthToken(loginResponseDto.authToken)
            sharedPref.saveUserEmail(userCredentialsDto.email)
            Pair(loginResponseDto.isDataAvailable, ApiResponse.OK)
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(null, getError(e))
        }
    }

    override suspend fun initialLoginUser(email: String, password: String): ApiResponse {
        val userCredentialsDto = UserCredentialsDto(email, password)
        return try {
            val loginResponseDto = authenticationApi.loginUser(userCredentialsDto)
            sharedPref.saveAuthToken(loginResponseDto.authToken)
            sharedPref.saveUserEmail(userCredentialsDto.email)
            //todo przekazywać tutaj imię otrzymane z API
            initialDataService.createMainPerson("User")
            ApiResponse.OK
        } catch (e: Exception) {
            e.printStackTrace()
            getError(e)
        }
    }

    override suspend fun useRemoteDataAfterLogin() {
        medicineDao.deleteAll()
        personDao.deleteAllWithoutMain()
        enqueueServerSync()
    }

    override suspend fun useLocalDataAfterLogin(): ApiResponse {
        return sharedPref.getAuthToken()?.let { authToken ->
            try {
                registeredUserApi.deleteAllData(authToken)
                enqueueServerSync()
                ApiResponse.OK
            } catch (e: Exception) {
                e.printStackTrace()
                getError(e)
            }
        } ?: throw Exception("AuthToken not available")
    }

    override suspend fun changeUserPassword(newPassword: String): ApiResponse {
        return sharedPref.getAuthToken()?.let { authToken ->
            try {
                val newPasswordDto = NewPasswordDto(value = newPassword)
                registeredUserApi.changeUserPassword(authToken, newPasswordDto)
                null
            } catch (e: Exception) {
                e.printStackTrace()
                getError(e)
            }
        } ?: throw Exception("AuthToken not available")
    }

    override suspend fun connectWithPatron(connectionKey: String): ApiResponse {
        if (getAppMode() == AppMode.LOGGED) {
            logoutUser()
        }
        return try {
            val connectedPersonDto = authenticationApi.patronConnect(connectionKey)
            sharedPref.saveAuthToken(connectedPersonDto.authToken)
            sharedPref.deleteUserEmail()
            switchToConnectedDatabase()
            initConnectedPersonDatabase(connectedPersonDto)
            enqueueServerSync()
            ApiResponse.OK
        } catch (e: Exception) {
            e.printStackTrace()
            getError(e)
        }
    }

    override suspend fun logoutUser() {
        sharedPref.deleteAuthToken()
        sharedPref.deleteUserEmail()
        with(deletedHistory) {
            clearPersonHistory()
            clearMedicineHistory()
            clearMedicinePlanHistory()
            clearPlannedMedicineHistory()
        }
        with(personDao) {
            val entityList = this.getEntityList()
            entityList.forEach {
                it.synchronizedWithServer = false
            }
            this.update(entityList)
        }
        with(medicineDao) {
            val entityList = this.getEntityList()
            entityList.forEach {
                it.synchronizedWithServer = false
            }
            this.update(entityList)
        }
        with(medicinePlanDao) {
            val entityList = this.getEntityList()
            entityList.forEach {
                it.synchronizedWithServer = false
            }
            this.update(entityList)
        }
        with(plannedMedicineDao) {
            val entityList = this.getEntityList()
            entityList.forEach {
                it.synchronizedWithServer = false
            }
            this.update(entityList)
        }
    }

    override suspend fun cancelPatronConnection() {
        val medicineDao: MedicineDao = get()
        val personDao: PersonDao = get()
        sharedPref.deleteAuthToken()
        medicineDao.deleteAll()
        personDao.deleteAllWithMain()

        switchToMainDatabase()
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

    override fun getUserEmailLive() = sharedPref.getUserEmailLive()

    override fun getLastSyncTimeLive() = sharedPref.getLastSyncTimeLive()

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
                workManager.enqueue(syncWork)
            } ?: throw Exception("AuthToken not available")
        }
    }

    override fun updateLastSyncTime() {
        val currDate = Date()
        sharedPref.saveLastSyncTime(currDate)
    }

    private fun getError(e: Exception) = when (e) {
        is SocketTimeoutException -> ApiResponse.TIMEOUT
        is HttpException -> when (e.code()) {
            422 -> ApiResponse.INCORRECT_DATA
            409 -> ApiResponse.ALREADY_EXISTS
            404 -> ApiResponse.NOT_FOUND
            else -> ApiResponse.ERROR

        }
        else -> ApiResponse.ERROR
    }

    private suspend fun initConnectedPersonDatabase(connectedPersonDto: ConnectedPersonDto) {
        val mainPerson = PersonEntity(
            personRemoteId = connectedPersonDto.personRemoteId,
            personName = connectedPersonDto.personName,
            personColorResId = connectedPersonDto.personColorResId,
            mainPerson = true,
            synchronizedWithServer = true
        )
        val newPersonDao: PersonDao = get()
        newPersonDao.insert(mainPerson)
    }

    private fun getAppMode(authToken: String, email: String): AppMode {
        return when {
            authToken.isNotEmpty() && email.isNotEmpty() -> AppMode.LOGGED
            authToken.isNotEmpty() && email.isEmpty() -> AppMode.CONNECTED
            else -> AppMode.OFFLINE
        }
    }

    private fun switchToConnectedDatabase() {
        unloadKoinModules(
            listOf(
                viewModelModule,
                serviceModule,
                remoteDataModule,
                localDataModule,
                mainRoomModule
            )
        )
        loadKoinModules(
            listOf(
                connectedRoomModule,
                localDataModule,
                remoteDataModule,
                serviceModule,
                viewModelModule
            )
        )
    }

    private fun switchToMainDatabase() {
        unloadKoinModules(
            listOf(
                viewModelModule,
                serviceModule,
                remoteDataModule,
                localDataModule,
                connectedRoomModule
            )
        )
        loadKoinModules(
            listOf(
                mainRoomModule,
                localDataModule,
                remoteDataModule,
                serviceModule,
                viewModelModule
            )
        )
    }
}