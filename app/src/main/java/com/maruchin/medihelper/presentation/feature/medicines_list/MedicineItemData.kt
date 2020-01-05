package com.maruchin.medihelper.presentation.feature.medicines_list

import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.MedicineState
import com.maruchin.medihelper.domain.model.MedicineItem

data class MedicineItemData(
    val medicineId: String,
    val pictureName: String?,
    val medicineName: String,
    val medicineUnit: String,
    val stateData: StateData
) {
    companion object {

        fun fromDomainModel(model: MedicineItem): MedicineItemData {
            return MedicineItemData(
                medicineId = model.medicineId,
                pictureName = model.pictureName,
                medicineName = model.name,
                medicineUnit = model.unit,
                stateData = getStateData(model.state)
            )
        }

        private fun getStateData(model: MedicineState): StateData {
            val stateWeight = model.currState / model.packageSize
            return StateData(
                stateColorId = getStateColorId(model.type),
                stateWeight = stateWeight,
                stateEmptyWeight = 1 - stateWeight
            )
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

    data class StateData(
        val stateColorId: Int,
        val stateWeight: Float,
        val stateEmptyWeight: Float
    )
}