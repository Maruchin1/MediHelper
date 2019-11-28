package com.maruchin.medihelper.data

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.maruchin.medihelper.data.framework.SharedPrefLiveData

class SharedPref (context: Context) {
    companion object {
        private const val PREF_NAME = "app-shared-pref"
        private const val KEY_PROFILE_COLOR_SET = "key-person-color-res-id-set"
        private const val KEY_MEDICINE_UNIT_SET = "key-medicine-type-list"
    }

    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getProfileColorsList(): List<String> {
        return pref.getStringSet(KEY_PROFILE_COLOR_SET, null)?.toList() ?: emptyList()
    }

    fun getMedicineUnitList() = pref.getStringSet(KEY_MEDICINE_UNIT_SET, null)?.toList() ?: emptyList()

    fun getMedicineUnitListLive(): LiveData<List<String>> {
        return Transformations.map(
            SharedPrefLiveData(
                pref,
                KEY_MEDICINE_UNIT_SET,
                emptySet<String>()
            )
        ) {
            it.toList()
        }
    }

    fun saveProfileColorsList(list: List<String>) = pref.edit(true) {
        putStringSet(KEY_PROFILE_COLOR_SET, list.toMutableSet())
    }

    fun saveMedicineUnitList(list: List<String>) = pref.edit(commit = true) {
        putStringSet(KEY_MEDICINE_UNIT_SET, list.toMutableSet())
    }
}