package com.maruchin.medihelper.presentation.feature.medicine_details

import com.google.firebase.storage.StorageReference
import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.MedicineState
import com.maruchin.medihelper.domain.model.MedicineDetails
import com.maruchin.medihelper.presentation.utils.PicturesStorageRef

data class MedicineDetailsData(
    val medicineId: String,
    val pictureRef: StorageReference?,
    val medicineName: String,
    val medicineType: String?,
    val medicineUnit: String,
    val expireDateAvailable: Boolean,
    val expireDate: String?,
    val stateAvailable: Boolean,
    val state: StateData
) {
    companion object {

        fun fromDomainModel(model: MedicineDetails, picturesStorageRef: PicturesStorageRef): MedicineDetailsData {
            return MedicineDetailsData(
                medicineId = model.medicineId,
                pictureRef = model.pictureName?.let { name ->
                    picturesStorageRef.getPictureRef(name)
                },
                medicineName = model.name,
                medicineType = model.type,
                medicineUnit = model.unit,
                expireDateAvailable = isExpireDateAvailable(model.expireDate),
                expireDate = model.expireDate?.let { formatExpireDate(it) },
                stateAvailable = isStateAvailable(model.state),
                state = getStateData(model.state, model.unit)
            )
        }

        private fun isExpireDateAvailable(expireDate: AppExpireDate?): Boolean {
            return expireDate != null
        }

        private fun formatExpireDate(expireDate: AppExpireDate) = expireDate.formatString

        private fun isStateAvailable(state: MedicineState): Boolean {
            return state.packageSize != 0f
        }

        private fun getStateData(model: MedicineState, medicineUnit: String): StateData {
            val stateWeight = model.currState / model.packageSize
            return StateData(
                stateText = getStateText(model.type),
                stateColorId = getStateColorId(model.type),
                stateWeight = stateWeight,
                stateEmptyWeight = 1 - stateWeight,
                stateNumbersText = formatStateNumberText(model, medicineUnit)
            )
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

        private fun formatStateNumberText(model: MedicineState, medicineUnit: String): String {
            return "${model.currState}/${model.packageSize} $medicineUnit"
        }

        data class StateData(
            val stateText: String,
            val stateColorId: Int,
            val stateWeight: Float,
            val stateEmptyWeight: Float,
            val stateNumbersText: String
        )
    }
}