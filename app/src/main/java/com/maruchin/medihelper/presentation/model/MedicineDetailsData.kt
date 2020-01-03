package com.maruchin.medihelper.presentation.model

import com.google.firebase.storage.StorageReference
import com.maruchin.medihelper.domain.entities.AppExpireDate
import com.maruchin.medihelper.domain.entities.MedicineState
import com.maruchin.medihelper.domain.model.MedicineDetails
import com.maruchin.medihelper.presentation.utils.PicturesRef

data class MedicineDetailsData(
    val medicineId: String,
    val pictureRef: StorageReference?,
    val medicineName: String,
    val medicineUnit: String,
    val expireDate: String,
    val daysRemain: String,
    val stateAvailable: Boolean,
    val state: MedicineStateData
) {
    companion object {

        fun fromDomainModel(medicineDetails: MedicineDetails, picturesRef: PicturesRef): MedicineDetailsData {
            return MedicineDetailsData(
                medicineId = medicineDetails.medicineId,
                pictureRef = medicineDetails.pictureName?.let { name ->
                    picturesRef.get(name)
                },
                medicineName = medicineDetails.name,
                medicineUnit = formatMedicineUnit(medicineDetails.unit),
                expireDate = formatExpireDate(medicineDetails.expireDate),
                daysRemain = formatDaysRemain(medicineDetails.daysRemains),
                stateAvailable = isStateAvailable(medicineDetails.state),
                state = MedicineStateData.fromDomainModel(medicineDetails.state, medicineDetails.unit)
            )
        }

        private fun formatMedicineUnit(unit: String) = "Jednostka: $unit"

        private fun formatExpireDate(expireDate: AppExpireDate) = expireDate.formatString

        private fun formatDaysRemain(daysRemain: Int) = "$daysRemain dni"

        private fun isStateAvailable(state: MedicineState): Boolean {
            return state.packageSize != 0f
        }
    }
}