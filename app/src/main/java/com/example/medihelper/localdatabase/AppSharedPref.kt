package com.example.medihelper.localdatabase

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.medihelper.custom.SharedPrefLiveData
import java.text.SimpleDateFormat
import java.util.*

interface AppSharedPref {
    fun getAuthToken(): String?
    fun getAuthTokenLive(): LiveData<String>
    fun getUserEmail(): String?
    fun getUserEmailLive(): LiveData<String>
    fun getLastSyncTimeLive(): LiveData<Date>
    fun getColorResIdList(): List<Int>
    fun getMedicineUnitList(): List<String>
    fun getMedicineUnitListLive(): LiveData<List<String>>
    fun saveAuthToken(authToken: String)
    fun saveUserEmail(userEmail: String)
    fun saveLastSyncTime(date: Date)
    fun saveColorResIdList(list: List<Int>)
    fun saveMedicineUnitList(list: List<String>)
    fun deleteAuthToken()
    fun deleteUserEmail()
}

class AppSharedPrefImpl(private val pref: SharedPreferences) : AppSharedPref {

    override fun getAuthToken(): String? = pref.getString(KEY_AUTH_TOKEN, null)

    override fun getAuthTokenLive(): LiveData<String> = SharedPrefLiveData(pref, KEY_AUTH_TOKEN, "")

    override fun getUserEmail(): String? = pref.getString(KEY_USER_EMAIL, null)

    override fun getUserEmailLive(): LiveData<String> = SharedPrefLiveData(pref, KEY_USER_EMAIL, "")

    override fun getLastSyncTimeLive(): LiveData<Date> {
        return Transformations.map(SharedPrefLiveData(pref, KEY_LAST_SYNC_TIME, "")) {
            if (it != null) SimpleDateFormat.getDateTimeInstance().parse(it) else null
        }
    }

    override fun getColorResIdList(): List<Int> {
        return pref.getStringSet(KEY_PERSON_COLOR_RES_ID_SET, null)?.map {
            it.toInt()
        } ?: emptyList()
    }

    override fun getMedicineUnitList() = pref.getStringSet(KEY_MEDICINE_UNIT_SET, null)?.toList() ?: emptyList()

    override fun getMedicineUnitListLive(): LiveData<List<String>> {
        return Transformations.map(SharedPrefLiveData(pref, KEY_MEDICINE_UNIT_SET, emptySet<String>())) {
            it.toList()
        }
    }

    override fun saveAuthToken(authToken: String) = pref.edit(true) {
        putString(KEY_AUTH_TOKEN, authToken)
    }

    override fun saveUserEmail(userEmail: String) = pref.edit(true) {
        putString(KEY_USER_EMAIL, userEmail)
    }

    override fun saveLastSyncTime(date: Date) {
        val dateString = SimpleDateFormat.getDateTimeInstance().format(date)
        pref.edit(true) {
            putString(KEY_LAST_SYNC_TIME, dateString)
        }
    }

    override fun saveColorResIdList(list: List<Int>) = pref.edit(true) {
        putStringSet(KEY_PERSON_COLOR_RES_ID_SET, list.map { it.toString() }.toMutableSet())
    }

    override fun saveMedicineUnitList(list: List<String>) = pref.edit(commit = true) {
        putStringSet(KEY_MEDICINE_UNIT_SET, list.toMutableSet())
    }

    override fun deleteAuthToken() = pref.edit(true) {
        putString(KEY_AUTH_TOKEN, null)
    }

    override fun deleteUserEmail() = pref.edit(true) {
        putString(KEY_USER_EMAIL, null)
    }

    companion object {
        const val KEY_AUTH_TOKEN = "key-auth-token"
        const val KEY_USER_EMAIL = "key-user-email"
        const val KEY_LAST_SYNC_TIME = "key-last-sync_time"
        private const val KEY_PERSON_COLOR_RES_ID_SET = "key-person-color-res-id-set"
        private const val KEY_MEDICINE_UNIT_SET = "key-medicine-type-list"
    }
}