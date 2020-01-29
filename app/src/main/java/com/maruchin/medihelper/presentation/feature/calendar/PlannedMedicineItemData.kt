package com.maruchin.medihelper.presentation.feature.calendar

import com.maruchin.medihelper.R
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.PlannedMedicineItem

data class PlannedMedicineItemData(
    val plannedMedicineId: String,
    val medicineName: String,
    val plannedDose: String,
    val plannedTime: String,
    val statusData: StatusData
) {

    companion object {

        fun fromDomainModel(model: PlannedMedicineItem): PlannedMedicineItemData {
            return PlannedMedicineItemData(
                plannedMedicineId = model.plannedMedicineId,
                medicineName = model.medicineName,
                plannedDose = formatDose(model.plannedDoseSize, model.medicineUnit),
                plannedTime = model.plannedTime.formatString,
                statusData = getStatusData(model.status)
            )
        }

        private fun formatDose(dose: Float, unit: String): String {
            return "$dose $unit"
        }

        private fun getStatusData(status: PlannedMedicine.Status): StatusData {
            return when (status) {
                PlannedMedicine.Status.TAKEN -> getStatusDataForTaken()
                PlannedMedicine.Status.NOT_TAKEN -> getStatusDataForNotTaken()
            }
        }

        private fun getStatusDataForTaken() = StatusData(
            colorId = R.color.colorStateGood,
            iconId = R.drawable.round_check_circle_24
        )

        private fun getStatusDataForNotTaken() = StatusData(
            colorId = R.color.colorDarkerGray,
            iconId = R.drawable.round_radio_button_unchecked_24
        )
    }

    data class StatusData(
        val colorId: Int,
        val iconId: Int
    )
}