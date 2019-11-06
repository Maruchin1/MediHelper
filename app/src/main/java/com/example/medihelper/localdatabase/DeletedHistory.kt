package com.example.medihelper.localdatabase

import android.content.SharedPreferences
import androidx.core.content.edit

interface DeletedHistory {
    fun getPersonHistory(): List<Long>
    fun getMedicineHistory(): List<Long>
    fun getMedicinePlanHistory(): List<Long>
    fun getPlannedMedicineHistory(): List<Long>
    fun addToPersonHistory(remoteId: Long)
    fun addToMedicineHistory(remoteId: Long)
    fun addToMedicinePlanHistory(remoteId: Long)
    fun addToPlannedMedicineHistory(remoteId: Long)
}

class DeletedHistoryImpl(
    private val sharedPreferences: SharedPreferences
) : DeletedHistory {

    companion object {
        private const val KEY_PERSON_HISTORY_SET = "key-person-history-set"
        private const val KEY_MEDICINE_HISTORY_SET = "key-medicine-history-set"
        private const val KEY_MEDICINE_PLAN_HISTORY_SET = "key-medicine-plan-history-set"
        private const val KEY_PLANNED_MEDICINE_HISTORY_SET = "key_planned-medicine-history-set"
    }

    override fun getPersonHistory() = getHistory(KEY_PERSON_HISTORY_SET)

    override fun getMedicineHistory() = getHistory(KEY_MEDICINE_HISTORY_SET)

    override fun getMedicinePlanHistory() = getHistory(KEY_MEDICINE_PLAN_HISTORY_SET)

    override fun getPlannedMedicineHistory() = getHistory(KEY_PLANNED_MEDICINE_HISTORY_SET)

    override fun addToPersonHistory(remoteId: Long) = addToHistory(KEY_PERSON_HISTORY_SET, remoteId)

    override fun addToMedicineHistory(remoteId: Long) = addToHistory(KEY_MEDICINE_HISTORY_SET, remoteId)

    override fun addToMedicinePlanHistory(remoteId: Long) = addToHistory(KEY_MEDICINE_PLAN_HISTORY_SET, remoteId)

    override fun addToPlannedMedicineHistory(remoteId: Long) = addToHistory(KEY_PLANNED_MEDICINE_HISTORY_SET, remoteId)

    private fun getHistory(key: String): List<Long> {
        return sharedPreferences.getStringSet(key, null)?.map {
            it.toLong()
        } ?: emptyList()
    }

    private fun addToHistory(key: String, remoteId: Long) {
        val set = sharedPreferences.getStringSet(key, null) ?: emptySet()
        set.add(remoteId.toString())
        sharedPreferences.edit(true) {
            putStringSet(key, set)
        }
    }
}