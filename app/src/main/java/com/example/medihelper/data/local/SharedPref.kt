package com.example.medihelper.data.local

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.custom.SharedPrefLiveData
import java.text.SimpleDateFormat
import java.util.*

class SharedPref (context: Context) {
    companion object {
        private const val PREF_NAME = "app-shared-pref"
        private const val KEY_AUTH_TOKEN = "key-auth-token"
        private const val KEY_USER_EMAIL = "key-user-email"
        private const val KEY_LAST_SYNC_TIME = "key-last-sync_time"
        private const val KEY_PERSON_COLOR_RES_ID_SET = "key-person-color-res-id-set"
        private const val KEY_MEDICINE_UNIT_SET = "key-medicine-type-list"
    }

    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getAuthToken(): String? = pref.getString(KEY_AUTH_TOKEN, null)

    fun getAuthTokenLive(): LiveData<String> = SharedPrefLiveData(pref, KEY_AUTH_TOKEN, "")

    fun getUserEmail(): String? = pref.getString(KEY_USER_EMAIL, null)

    fun getUserEmailLive(): LiveData<String> = SharedPrefLiveData(pref, KEY_USER_EMAIL, "")

    fun getLastSyncTimeLive(): LiveData<Date> {
        return Transformations.map(SharedPrefLiveData(pref, KEY_LAST_SYNC_TIME, "")) {
            if (it != null) SimpleDateFormat.getDateTimeInstance().parse(it) else null
        }
    }

    fun getPersonColorIdList(): List<Int> {
        return pref.getStringSet(KEY_PERSON_COLOR_RES_ID_SET, null)?.map {
            it.toInt()
        } ?: emptyList()
    }

    fun getMedicineUnitList() = pref.getStringSet(KEY_MEDICINE_UNIT_SET, null)?.toList() ?: emptyList()

    fun getMedicineUnitListLive(): LiveData<List<String>> {
        return Transformations.map(SharedPrefLiveData(pref, KEY_MEDICINE_UNIT_SET, emptySet<String>())) {
            it.toList()
        }
    }

    fun saveAuthToken(authToken: String) = pref.edit(true) {
        putString(KEY_AUTH_TOKEN, authToken)
    }

    fun saveUserEmail(userEmail: String) = pref.edit(true) {
        putString(KEY_USER_EMAIL, userEmail)
    }

    fun saveLastSyncTime(date: Date) {
        val dateString = SimpleDateFormat.getDateTimeInstance().format(date)
        pref.edit(true) {
            putString(KEY_LAST_SYNC_TIME, dateString)
        }
    }

    fun savePersonColorIdList(list: List<Int>) = pref.edit(true) {
        putStringSet(KEY_PERSON_COLOR_RES_ID_SET, list.map { it.toString() }.toMutableSet())
    }

    fun saveMedicineUnitList(list: List<String>) = pref.edit(commit = true) {
        putStringSet(KEY_MEDICINE_UNIT_SET, list.toMutableSet())
    }

    fun deleteAuthToken() = pref.edit(true) {
        putString(KEY_AUTH_TOKEN, null)
    }

    fun deleteUserEmail() = pref.edit(true) {
        putString(KEY_USER_EMAIL, null)
    }
}