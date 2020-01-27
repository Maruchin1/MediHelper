package com.maruchin.medihelper.presentation.feature.planned_medicine_options

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.model.PlannedMedicineItem

data class PlannedMedicineBasicData(
    val medicinePlanId: String,
    val medicineName: String,
    val medicineType: String,
    val plannedDateTime: String,
    val plannedDose: String
) {

    companion object {

        fun fromDomainModel(model: PlannedMedicineItem) = PlannedMedicineBasicData(
            medicinePlanId = model.medicinePlanId,
            medicineName = model.medicineName,
            medicineType = model.medicineType,
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