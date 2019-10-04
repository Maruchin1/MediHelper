package com.example.medihelper.services

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.R
import com.example.medihelper.custom.SharedPrefLiveData
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.repositories.PersonRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    fun getLoggedUserAuthToken(): String? = sharedPreferences.getString(KEY_LOGGED_USER_AUTH_TOKEN, null)

    fun getAppModeLive(): LiveData<AppMode> {
        val authTokenLive = SharedPrefLiveData(sharedPreferences, KEY_LOGGED_USER_AUTH_TOKEN, "")
        val emailLive = SharedPrefLiveData(sharedPreferences, KEY_LOGGED_USER_EMAIL, "")
        var authToken = ""
        var email = ""
//        val appModeLive = MediatorLiveData<AppMode>()
//        appModeLive.addSource(authTokenLive) { newAuthToken ->
//            authToken = newAuthToken
//            appModeLive.postValue(AppMode.getAppMode(authToken, email))
//        }
//        appModeLive.addSource(emailLive) { newEmail ->
//            email = newEmail
//            appModeLive.postValue(AppMode.getAppMode(authToken, email))
//        }
        val appModeLive = MutableLiveData(AppMode.CONNECTED)
        return appModeLive
    }

    fun getLoggedUserEmailLive(): LiveData<String> = SharedPrefLiveData(sharedPreferences, KEY_LOGGED_USER_EMAIL, "")

    fun getLastSyncTimeLive(): LiveData<Date> =
        Transformations.map(SharedPrefLiveData(sharedPreferences, KEY_LAST_SYNC_TIME, "")) { dateString ->
            if (dateString != "") SimpleDateFormat.getDateTimeInstance().parse(dateString) else null
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

    fun saveLoggedUserAuthToken(newAuthToken: String) = sharedPreferences.edit(true) {
        putString(KEY_LOGGED_USER_AUTH_TOKEN, newAuthToken)
    }

    fun saveLoggedUserEmail(newEmail: String) = sharedPreferences.edit(true) {
        putString(KEY_LOGGED_USER_EMAIL, newEmail)
    }

    fun saveLastSyncTimeLive(dateTime: Date) {
        val dateString = SimpleDateFormat.getDateTimeInstance().format(dateTime)
        sharedPreferences.edit(true) {
            putString(KEY_LAST_SYNC_TIME, dateString)
        }
    }

    // Delete
    fun deleteLoggedUserAuthToken() = sharedPreferences.edit(true) {
        putString(KEY_LOGGED_USER_AUTH_TOKEN, null)
    }

    fun deleteLoggedUserEmail() = sharedPreferences.edit(true) {
        putString(KEY_LOGGED_USER_EMAIL, null)
    }

    companion object {
        private const val KEY_MEDICINE_UNIT_SET = "key-medicine-type-list"
        private const val KEY_PERSON_COLOR_RES_ID_SET = "key-person-color-res-id-array"
        private const val KEY_LOGGED_USER_AUTH_TOKEN = "key-logged-user-auth-token"
        private const val KEY_LOGGED_USER_EMAIL = "key-logged-user-email"
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