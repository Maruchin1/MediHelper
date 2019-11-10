package com.example.medihelper.service

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.work.*
import com.example.medihelper.MainApplication
import com.example.medihelper.R
import com.example.medihelper.custom.SharedPrefLiveData
import com.example.medihelper.localdatabase.dao.MedicineDao
import com.example.medihelper.localdatabase.dao.PersonDao
import com.example.medihelper.localdatabase.entity.PersonEntity
import com.example.medihelper.remotedatabase.api.AuthenticationApi
import com.example.medihelper.remotedatabase.api.RegisteredUserApi
import com.example.medihelper.remotedatabase.dto.ConnectedPersonDto
import com.example.medihelper.remotedatabase.dto.NewPasswordDto
import com.example.medihelper.remotedatabase.dto.UserCredentialsDto
import com.example.medihelper.serversync.ConnectedPersonSyncWorker
import com.example.medihelper.serversync.LoggedUserSyncWorker
import com.example.medihelper.serversync.ServerSyncWorker
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Exception

interface ServerApiService {
    fun getAppMode(): AppMode
    fun getAppModeLive(): LiveData<AppMode>
    fun getUserEmailLive(): LiveData<String>
    fun getLastSyncTimeLive(): LiveData<Date>
    fun updateLastSyncTime()
    fun enqueueServerSync()
    suspend fun registerNewUser(email: String, password: String): String?
    suspend fun loginUser(email: String, password: String): String?
    suspend fun isRemoteDataAvailable(): Pair<Boolean?, String?>
    suspend fun useRemoteDataAfterLogin()
    suspend fun useLocalDataAfterLogin(): String?
    suspend fun changeUserPassword(newPassword: String): String?
    suspend fun connectWithPatron(connectionKey: String): String?
    suspend fun logoutUser()
    suspend fun cancelPatronConnection()
}

enum class AppMode {
    OFFLINE, LOGGED, CONNECTED
}

class ServerApiServiceImpl(
    private val mainApplication: MainApplication,
    private val sharedPreferences: SharedPreferences,
    private val authenticationApi: AuthenticationApi,
    private val registeredUserApi: RegisteredUserApi,
    private val personDao: PersonDao,
    private val medicineDao: MedicineDao,
    private val workManager: WorkManager
) : ServerApiService {

    companion object {
        private const val KEY_AUTH_TOKEN = "key-auth-token"
        private const val KEY_USER_EMAIL = "key-user-email"
        private const val KEY_LAST_SYNC_TIME = "key-last-sync_time"
    }

    override fun getAppMode(): AppMode {
        val authToken = getAuthToken() ?: ""
        val email = getUserEmail() ?: ""
        return getAppMode(authToken, email)
    }

    override suspend fun registerNewUser(email: String, password: String): String? {
        val userCredentialsDto = UserCredentialsDto(email, password)
        return try {
            authenticationApi.registerNewUser(userCredentialsDto)
            null
        } catch (e: Exception) {
            e.printStackTrace()
            getErrorMessage(e)
        }
    }

    override suspend fun loginUser(email: String, password: String): String? {
        val userCredentialsDto = UserCredentialsDto(email, password)
        return try {
            val authToken = authenticationApi.loginUser(userCredentialsDto)
            saveAuthToken(authToken)
            saveUserEmail(userCredentialsDto.email)
            null
        } catch (e: Exception) {
            e.printStackTrace()
            getErrorMessage(e)
        }
    }

    override suspend fun isRemoteDataAvailable(): Pair<Boolean?, String?> {
        return getAuthToken()?.let { authToken ->
            try {
                val available = registeredUserApi.isDataAvailable(authToken)
                Pair(available, null)
            } catch (e: Exception) {
                e.printStackTrace()
                Pair(null, getErrorMessage(e))
            }
        } ?: throw Exception("AuthToken not available")
    }

    override suspend fun useRemoteDataAfterLogin() {
        medicineDao.deleteAll()
        personDao.deleteAllWithoutMain()
        enqueueServerSync()
    }

    override suspend fun useLocalDataAfterLogin(): String? {
        return getAuthToken()?.let { authToken ->
            try {
                registeredUserApi.deleteAllData(authToken)
                enqueueServerSync()
                null
            } catch (e: Exception) {
                e.printStackTrace()
                getErrorMessage(e)
            }
        } ?: throw Exception("AuthToken not available")
    }

    override suspend fun changeUserPassword(newPassword: String): String? {
        return getAuthToken()?.let { authToken ->
            try {
                val newPasswordDto = NewPasswordDto(value = newPassword)
                registeredUserApi.changeUserPassword(authToken, newPasswordDto)
                null
            } catch (e: Exception) {
                e.printStackTrace()
                getErrorMessage(e)
            }
        } ?: throw Exception("AuthToken not available")
    }

    override suspend fun connectWithPatron(connectionKey: String): String? {
        return getAuthToken()?.let { authToken ->
            try {
                val connectedPersonDto = authenticationApi.patronConnect(connectionKey)
                saveAuthToken(connectedPersonDto.authToken)
                deleteUserEmail()
                mainApplication.switchToConnectedPersonDatabase()
                initConnectedPersonDatabase(connectedPersonDto)
                null
            } catch (e: Exception) {
                e.printStackTrace()
                getErrorMessage(e)
            }
        } ?: throw Exception("AuthToken not available")
    }

    override suspend fun logoutUser() {
        deleteAuthToken()
        deleteUserEmail()
        //todo czyścić historię i ustawiać wszystko na synchronized = false
    }

    override suspend fun cancelPatronConnection() {
        deleteAuthToken()
        medicineDao.deleteAll()
        personDao.deleteAllWithMain()
        mainApplication.switchToMainDatabase()
    }

    override fun getAppModeLive(): LiveData<AppMode> {
        val authTokenLive = SharedPrefLiveData(sharedPreferences, KEY_AUTH_TOKEN, "")
        val emailLive = SharedPrefLiveData(sharedPreferences, KEY_USER_EMAIL, "")
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

    override fun getUserEmailLive() = SharedPrefLiveData(sharedPreferences, KEY_USER_EMAIL, "")

    override fun getLastSyncTimeLive(): LiveData<Date> {
        return Transformations.map(SharedPrefLiveData(sharedPreferences, KEY_LAST_SYNC_TIME, "")) {
            if (it != null) SimpleDateFormat.getDateTimeInstance().parse(it) else null
        }
    }

    override fun enqueueServerSync() {
        val syncWorkBuilder = when (getAppMode()) {
            AppMode.LOGGED -> OneTimeWorkRequestBuilder<LoggedUserSyncWorker>()
            AppMode.CONNECTED -> OneTimeWorkRequestBuilder<ConnectedPersonSyncWorker>()
            else -> null
        }
        if (syncWorkBuilder != null) {
            getAuthToken()?.let { authToken ->
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
        val currDateString = SimpleDateFormat.getDateTimeInstance().format(currDate)
        sharedPreferences.edit(true) {
            putString(KEY_LAST_SYNC_TIME, currDateString)
        }
    }

    private fun getErrorMessage(e: Exception): String {
        val messageId = when (e) {
            is SocketTimeoutException -> R.string.error_timeout
            is HttpException -> when (e.code()) {
                422 -> R.string.error_incorrect_credentials
                409 -> R.string.error_user_exists
                404 -> R.string.error_user_not_found
                else -> R.string.error_connection

            }
            else -> R.string.error_connection
        }
        return mainApplication.resources.getString(messageId)
    }

    private suspend fun initConnectedPersonDatabase(connectedPersonDto: ConnectedPersonDto) {
        val mainPerson = PersonEntity(
            personRemoteID = connectedPersonDto.personRemoteId,
            personName = connectedPersonDto.personName,
            personColorResID = connectedPersonDto.personColorResId,
            mainPerson = true
        )
        personDao.insert(mainPerson)
    }

    private fun getAppMode(authToken: String, email: String): AppMode {
        return when {
            authToken.isNotEmpty() && email.isNotEmpty() -> AppMode.LOGGED
            authToken.isNotEmpty() && email.isEmpty() -> AppMode.CONNECTED
            else -> AppMode.OFFLINE
        }
    }

    private fun getAuthToken() = sharedPreferences.getString(KEY_AUTH_TOKEN, null)

    private fun getUserEmail() = sharedPreferences.getString(KEY_USER_EMAIL, null)

    private fun saveAuthToken(authToken: String) = sharedPreferences.edit(true) {
        putString(KEY_AUTH_TOKEN, authToken)
    }

    private fun saveUserEmail(email: String) = sharedPreferences.edit(true) {
        putString(KEY_USER_EMAIL, email)
    }

    private fun deleteAuthToken() = sharedPreferences.edit(true) {
        putString(KEY_AUTH_TOKEN, null)
    }

    private fun deleteUserEmail() = sharedPreferences.edit(true) {
        putString(KEY_USER_EMAIL, null)
    }
}