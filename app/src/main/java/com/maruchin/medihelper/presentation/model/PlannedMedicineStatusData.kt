package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.PlannedMedicine

data class PlannedMedicineStatusData(
    val text: String,
    val colorId: Int,
    val iconId: Int
) {

    companion object {

        fun fromDomainModel(status: PlannedMedicine.Status): PlannedMedicineStatusData {
            return when (status) {
                PlannedMedicine.Status.TAKEN -> getTakenData()
                PlannedMedicine.Status.NOT_TAKEN -> getNotTakenData()
            }
        }

        private fun getTakenData(): PlannedMedicineStatusData {
            return PlannedMedicineStatusData(
                text = "Przyjęty",
                colorId = R.color.colorStateGood,
                iconId = R.drawable.round_check_circle_24
            )
        }

        private fun getNotTakenData(): PlannedMedicineStatusData {
            return PlannedMedicineStatusData(
                text = "Nieprzyjęty",
                colorId = R.color.colorDarkerGray,
                iconId = R.drawable.round_radio_button_unchecked_24
            )
        }
    }
}