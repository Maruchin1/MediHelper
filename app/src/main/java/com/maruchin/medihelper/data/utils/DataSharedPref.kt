package com.maruchin.medihelper.data.utils

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.maruchin.medihelper.data.framework.SharedPrefLiveData

class DataSharedPref(context: Context) {

    companion object {
        private const val PREF_NAME = "data-shared-pref"
        private const val KEY_PROFILE_COLOR_SET = "key-person-color-res-id-set"
        private const val KEY_MEDICINE_UNIT_SET = "key-medicine-type-list"
    }

    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val defaultProfileColors: List<String> by lazy {
        listOf(
            ProfileColor.MAIN,
            ProfileColor.PURPLE,
            ProfileColor.BLUE,
            ProfileColor.BROWN,
            ProfileColor.CYAN,
            ProfileColor.GRAY,
            ProfileColor.LIGHT_GREEN,
            ProfileColor.ORANGE,
            ProfileColor.YELLOW
        ).map { it.colorString }
    }
    private val defaultMedicinesUnits: List<String> by lazy {
        listOf("dawki", "tabletki", "ml", "g", "mg", "pastylki")
    }

    init {
        checkDefaultProfileColors()
        checkDefaultMedicineUnits()
    }

    fun getProfileColorsList(): List<String> {
        return pref.getStringSet(KEY_PROFILE_COLOR_SET, null)?.toList() ?: emptyList()
    }

    fun getMedicineUnitList() = pref.getStringSet(KEY_MEDICINE_UNIT_SET, null)?.toList() ?: emptyList()

    fun saveProfileColorsList(list: List<String>) = pref.edit(true) {
        putStringSet(KEY_PROFILE_COLOR_SET, list.toMutableSet())
    }

    fun saveMedicineUnitList(list: List<String>) = pref.edit(commit = true) {
        putStringSet(KEY_MEDICINE_UNIT_SET, list.toMutableSet())
    }

    private fun checkDefaultProfileColors() {
        val profileColors = getProfileColorsList()
        if (profileColors.isNullOrEmpty()) {
            saveProfileColorsList(defaultProfileColors)
        }
    }

    private fun checkDefaultMedicineUnits() {
        val medicineUnits = getMedicineUnitList()
        if (medicineUnits.isNullOrEmpty()) {
            saveMedicineUnitList(defaultMedicinesUnits)
        }
    }
}