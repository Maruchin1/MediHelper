package com.example.medihelper.services

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.custom.SharedPrefLiveData
import java.text.SimpleDateFormat
import java.util.*

class SharedPrefService(
    private val sharedPreferences: SharedPreferences
) {
    private val TAG = "SharedPrefService"

    // Get
    fun getMedicineUnitList() = sharedPreferences.getStringSet(KEY_MEDICINE_UNIT_SET, null)?.toList() ?: emptyList()

    fun getPersonColorResIDList() =
        sharedPreferences.getStringSet(KEY_PERSON_COLOR_RES_ID_SET, null)?.map { stringValue ->
            stringValue.toInt()
        } ?: emptyList()

    fun getAuthToken(): String? = sharedPreferences.getString(KEY_AUTH_TOKEN, null)

    fun getAppMode(): AppMode {
        val authToken = sharedPreferences.getString(KEY_AUTH_TOKEN, "") ?: ""
        val email = sharedPreferences.getString(KEY_USER_EMAIL, "") ?: ""
        return AppMode.getAppMode(authToken, email)
    }

    fun getMedicineUnitListLive(): LiveData<List<String>> =
        Transformations.map(SharedPrefLiveData(sharedPreferences, KEY_MEDICINE_UNIT_SET, emptySet<String>())) {
            it.toList()
        }

    fun getUserEmailLive(): LiveData<String> = SharedPrefLiveData(sharedPreferences, KEY_USER_EMAIL, "")

    fun getLastSyncTimeLive(): LiveData<Date> =
        Transformations.map(SharedPrefLiveData(sharedPreferences, KEY_LAST_SYNC_TIME, "")) { dateString ->
            if (dateString != "") SimpleDateFormat.getDateTimeInstance().parse(dateString) else null
        }

    fun getAppModeLive(): LiveData<AppMode> {
        val authTokenLive = SharedPrefLiveData(sharedPreferences, KEY_AUTH_TOKEN, "")
        val emailLive = SharedPrefLiveData(sharedPreferences, KEY_USER_EMAIL, "")
        var authToken = ""
        var email = ""
        val appModeLive = MediatorLiveData<AppMode>()
        appModeLive.addSource(authTokenLive) { newAuthToken ->
            authToken = newAuthToken
            appModeLive.postValue(AppMode.getAppMode(authToken, email))
        }
        appModeLive.addSource(emailLive) { newEmail ->
            email = newEmail
            appModeLive.postValue(AppMode.getAppMode(authToken, email))
        }
        return appModeLive
    }

    // Save
    fun saveMedicineUnitList(newMedicineUnitList: List<String>) = sharedPreferences.edit(true) {
        putStringSet(KEY_MEDICINE_UNIT_SET, newMedicineUnitList.toMutableSet())
    }

    fun savePersonColorResIDList(newPersonColorResIDList: List<Int>) = sharedPreferences.edit(true) {
        putStringSet(
            KEY_PERSON_COLOR_RES_ID_SET,
            newPersonColorResIDList.map { personColorResID ->
                personColorResID.toString()
            }.toMutableSet()
        )
    }

    fun saveAuthToken(newAuthToken: String) = sharedPreferences.edit(true) {
        putString(KEY_AUTH_TOKEN, newAuthToken)
    }

    fun saveUserEmail(newEmail: String) = sharedPreferences.edit(true) {
        putString(KEY_USER_EMAIL, newEmail)
    }

    fun saveLastSyncTimeLive(dateTime: Date) {
        val dateString = SimpleDateFormat.getDateTimeInstance().format(dateTime)
        sharedPreferences.edit(true) {
            putString(KEY_LAST_SYNC_TIME, dateString)
        }
    }

    // Delete
    fun deleteAuthToken() = sharedPreferences.edit(true) {
        putString(KEY_AUTH_TOKEN, null)
    }

    fun deleteUserEmail() = sharedPreferences.edit(true) {
        putString(KEY_USER_EMAIL, null)
    }

    companion object {
        private const val KEY_MEDICINE_UNIT_SET = "key-medicine-type-list"
        private const val KEY_PERSON_COLOR_RES_ID_SET = "key-person-color-res-id-array"
        private const val KEY_AUTH_TOKEN = "key-auth-token"
        private const val KEY_USER_EMAIL = "key-user-email"
        private const val KEY_LAST_SYNC_TIME = "key-last-sync_time"
    }

    enum class AppMode {
        OFFLINE, LOGGED, CONNECTED;

        companion object {
            fun getAppMode(authToken: String, email: String) = when {
                authToken.isNotEmpty() && email.isNotEmpty() -> LOGGED
                authToken.isNotEmpty() && email.isEmpty() -> CONNECTED
                else -> OFFLINE
            }
        }
    }
}