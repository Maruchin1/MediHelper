package com.example.medihelper.services

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import com.example.medihelper.R
import com.example.medihelper.custom.SharedPrefLiveData
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.repositories.PersonRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SharedPrefService(
    private val sharedPreferences: SharedPreferences
) {
    private val TAG = "SharedPrefService"

    // Get
    fun getMedicineUnitList() = sharedPreferences.getStringSet(KEY_MEDICINE_UNIT_SET, null)?.toList() ?: emptyList()

    fun getPersonColorResIDList() = sharedPreferences.getStringSet(KEY_PERSON_COLOR_RES_ID_SET, null)?.map { stringValue ->
        stringValue.toInt()
    } ?: emptyList()

    fun getLoggedUserAuthToken() = sharedPreferences.getString(KEY_LOGGED_USER_AUTH_TOKEN, null)

    fun getLoggedUserEmailLive(): LiveData<String> = SharedPrefLiveData(sharedPreferences, KEY_LOGGED_USER_EMAIL, "")

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
    }
}