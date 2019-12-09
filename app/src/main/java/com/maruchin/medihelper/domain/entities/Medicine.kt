package com.maruchin.medihelper.domain.entities

import com.maruchin.medihelper.R
import java.io.File

data class Medicine(
    val medicineId: Int,
    val name: String,
    val unit: String,
    val expireDate: AppExpireDate,
    val packageSize: Float?,
    var currState: Float?,
    val additionalInfo: String?,
    val image: File?
) {
    fun reduceCurrState(doseSize: Float) {
        val currState = this.currState
        if (currState != null) {
            var newState = currState - doseSize
            if (newState < 0f) {
                newState = 0f
            }
            this.currState = newState
        }
    }

    fun increaseCurrState(doseSize: Float) {
        val currState = this.currState
        val packageSize = this.packageSize
        if (currState != null) {
            var newState = currState + doseSize
            if (packageSize != null && newState > packageSize) {
                newState = packageSize
            }
            this.currState = newState
        }
    }

    fun getStateData() : MedicineStateData {
        val state = this.packageSize?.let { this.currState?.div(it) }
        return MedicineStateData(
            stateAvailable = state != null,
            stateWeight = state,
            emptyWeight = state?.let { 1 - it },
            colorId = state?.let {
                when {
                    it >= STATE_GOOD_LIMIT -> R.color.colorStateGood
                    it > STATE_MEDIUM_LIMIT -> R.color.colorStateMedium
                    else -> R.color.colorStateSmall
                }
            },
            text = state?.let {
                when {
                    it >= STATE_GOOD_LIMIT -> TEXT_STATE_GOOD
                    it > STATE_MEDIUM_LIMIT -> TEXT_STATE_MEDIUM
                    it == 0f -> TEXT_STATE_EMPTY
                    else -> TEXT_STATE_SMALL
                }
            },
            numberText = "${currState}/${packageSize}"
        )
    }

    companion object {
        private const val STATE_GOOD_LIMIT = 0.75f
        private const val STATE_MEDIUM_LIMIT = 0.4f
        private const val TEXT_STATE_GOOD = "Duży zapas"
        private const val TEXT_STATE_MEDIUM = "Średnia ilość"
        private const val TEXT_STATE_SMALL = "Blisko końca"
        private const val TEXT_STATE_EMPTY = "Brak"
    }
}