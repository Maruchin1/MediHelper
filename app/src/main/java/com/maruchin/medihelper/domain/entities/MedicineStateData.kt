package com.maruchin.medihelper.domain.entities

import com.maruchin.medihelper.R

data class MedicineStateData(
    val stateWeight: Float?,
    val emptyWeight: Float?,
    val colorId: Int?,
    val text: String?,
    val numberText: String?
) {
    companion object {
        fun get(packageSize: Float?, currState: Float?): MedicineStateData? {
            if (packageSize == null || currState == null) {
                return null
            } else {
                val state = currState / packageSize
                return MedicineStateData(
                    stateWeight = state,
                    emptyWeight = state.let { 1 - it },
                    colorId = state.let {
                        when {
                            it >= STATE_GOOD_LIMIT -> R.color.colorStateGood
                            it > STATE_MEDIUM_LIMIT -> R.color.colorStateMedium
                            else -> R.color.colorStateSmall
                        }
                    },
                    text = state.let {
                        when {
                            it >= STATE_GOOD_LIMIT -> TEXT_STATE_GOOD
                            it > STATE_MEDIUM_LIMIT -> TEXT_STATE_MEDIUM
                            else -> TEXT_STATE_SMALL
                        }
                    },
                    numberText = "${currState}/${packageSize}"
                )
            }
        }

        private const val STATE_GOOD_LIMIT = 0.75f
        private const val STATE_MEDIUM_LIMIT = 0.4f
        private const val TEXT_STATE_GOOD = "Duży zapas"
        private const val TEXT_STATE_MEDIUM = "Średnia ilość"
        private const val TEXT_STATE_SMALL = "Blisko końca"
    }
}