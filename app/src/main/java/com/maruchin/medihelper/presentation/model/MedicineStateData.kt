package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.MedicineState

data class MedicineStateData(
    val stateText: String,
    val stateColorId: Int,
    val stateWeight: Float,
    val stateEmptyWeight: Float,
    val stateNumbersText: String
) {
    companion object {

        fun fromDomainModel(medicineState: MedicineState, medicineUnit: String): MedicineStateData {
            val stateWeight = calcStateWeight(medicineState)
            return MedicineStateData(
                stateText = getStateText(medicineState.type),
                stateColorId = getStateColorId(medicineState.type),
                stateWeight = stateWeight,
                stateEmptyWeight = 1 - stateWeight,
                stateNumbersText = "${medicineState.currState}/${medicineState.packageSize} $medicineUnit"
            )
        }

        private fun calcStateWeight(domainModel: MedicineState): Float {
            return domainModel.currState / domainModel.packageSize
        }

        private fun getStateText(stateType: MedicineState.Type): String {
            return when (stateType) {
                MedicineState.Type.GOOD -> "Duży zapas"
                MedicineState.Type.MEDIUM -> "Średnia ilość"
                MedicineState.Type.SMALL -> "Blisko końca"
                MedicineState.Type.EMPTY -> "Brak"
            }
        }

        private fun getStateColorId(stateType: MedicineState.Type): Int {
            return when (stateType) {
                MedicineState.Type.GOOD -> R.color.colorStateGood
                MedicineState.Type.MEDIUM -> R.color.colorStateMedium
                MedicineState.Type.SMALL -> R.color.colorStateSmall
                MedicineState.Type.EMPTY -> R.color.colorStateSmall
            }
        }
    }
}