package com.example.medihelper.data.local

import android.content.Context
import androidx.core.content.edit

class DeletedHistory(context: Context) {

    companion object {
        private const val PREF_NAME = "deleted-history-shared-pref"
        private const val KEY_PERSON_HISTORY_SET = "key-person-history-set"
        private const val KEY_MEDICINE_HISTORY_SET = "key-medicine-history-set"
        private const val KEY_MEDICINE_PLAN_HISTORY_SET = "key-medicine-plan-history-set"
        private const val KEY_PLANNED_MEDICINE_HISTORY_SET = "key_planned-medicine-history-set"
    }

    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getPersonHistory() = getHistory(KEY_PERSON_HISTORY_SET)

    fun getMedicineHistory() = getHistory(KEY_MEDICINE_HISTORY_SET)

    fun getMedicinePlanHistory() = getHistory(KEY_MEDICINE_PLAN_HISTORY_SET)

    fun getPlannedMedicineHistory() = getHistory(KEY_PLANNED_MEDICINE_HISTORY_SET)

    fun addToPersonHistory(remoteId: Long) = addToHistory(KEY_PERSON_HISTORY_SET, remoteId)

    fun addToMedicineHistory(remoteId: Long) = addToHistory(KEY_MEDICINE_HISTORY_SET, remoteId)

    fun addToMedicinePlanHistory(remoteId: Long) = addToHistory(KEY_MEDICINE_PLAN_HISTORY_SET, remoteId)

    fun addToPlannedMedicineHistory(remoteId: Long) = addToHistory(KEY_PLANNED_MEDICINE_HISTORY_SET, remoteId)

    fun clearPersonHistory() = clearHistory(KEY_PERSON_HISTORY_SET)

    fun clearMedicineHistory() = clearHistory(KEY_MEDICINE_HISTORY_SET)

    fun clearMedicinePlanHistory() = clearHistory(KEY_MEDICINE_PLAN_HISTORY_SET)

    fun clearPlannedMedicineHistory() = clearHistory(KEY_PLANNED_MEDICINE_HISTORY_SET)

    fun clearAllHitory() {
        arrayOf(
            KEY_PERSON_HISTORY_SET,
            KEY_MEDICINE_HISTORY_SET,
            KEY_MEDICINE_PLAN_HISTORY_SET,
            KEY_PLANNED_MEDICINE_HISTORY_SET
        ).forEach {
            clearHistory(it)
        }
    }

    private fun getHistory(key: String): List<Long> {
        return pref.getStringSet(key, null)?.map {
            it.toLong()
        } ?: emptyList()
    }

    private fun addToHistory(key: String, remoteId: Long) {
        val set = pref.getStringSet(key, null) ?: emptySet()
        val newSet = set.toMutableSet()
        newSet.add(remoteId.toString())
        pref.edit(true) {
            putStringSet(key, newSet)
        }
    }

    private fun clearHistory(key: String) {
        pref.edit(true) {
            putStringSet(key, null)
        }
    }
}