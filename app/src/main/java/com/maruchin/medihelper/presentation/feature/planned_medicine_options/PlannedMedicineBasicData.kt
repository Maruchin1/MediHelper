package com.maruchin.medihelper.presentation.feature.planned_medicine_options

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.model.PlannedMedicineDetails

data class PlannedMedicineBasicData(
    val medicinePlanId: String,
    val medicineName: String,
    val plannedDateTime: String,
    val plannedDose: String
) {

    companion object {

        fun fromDomainModel(model: PlannedMedicineDetails) = PlannedMedicineBasicData(
            medicinePlanId = model.medicinePlanId,
            medicineName = model.medicineName,
            plannedDateTime = formatDateTime(model.plannedDate, model.plannedTime),
            plannedDose = formatDose(model.plannedDoseSize, model.medicineUnit)
        )

        private fun formatDateTime(date: AppDate, time: AppTime): String {
            return "Zaplanowano na ${date.formatString} godz. ${time.formatString}"
        }

        private fun formatDose(doseSize: Float, unit: String): String {
            return "Do przyjęcia $doseSize $unit"
        }
    }
}