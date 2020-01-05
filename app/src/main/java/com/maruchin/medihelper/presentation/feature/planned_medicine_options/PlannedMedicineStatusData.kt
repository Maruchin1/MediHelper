package com.maruchin.medihelper.presentation.feature.planned_medicine_options

import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.PlannedMedicine

data class PlannedMedicineStatusData(
    val statusText: String,
    val statusColorId: Int,
    val statusIconId: Int,
    val changeStateText: String,
    val changeStateIconId: Int
) {
    companion object {

        fun fromDomainModel(status: PlannedMedicine.Status): PlannedMedicineStatusData {
            return when (status) {
                PlannedMedicine.Status.TAKEN -> getForTaken()
                PlannedMedicine.Status.NOT_TAKEN -> getForNotTaken()
            }
        }

        private fun getForTaken() = PlannedMedicineStatusData(
            statusText = "Przyjęty",
            statusColorId = R.color.colorStateGood,
            statusIconId = R.drawable.round_check_circle_24,
            changeStateText = "Anuluj przyjęcie leku",
            changeStateIconId = R.drawable.round_close_24
        )

        private fun getForNotTaken() = PlannedMedicineStatusData(
            statusText = "Nieprzyjęty",
            statusColorId = R.color.colorDarkerGray,
            statusIconId = R.drawable.round_radio_button_unchecked_24,
            changeStateText = "Oznacz jako przyjęty",
            changeStateIconId = R.drawable.baseline_check_24
        )
    }
}