package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.model.MedicinePlanItem

data class MedicinePlanItemData(
    val medicinePlanId: String,
    val medicineName: String,
    val medicineUnit: String,
    val planDuration: String
) {

    companion object {

        fun fromDomainModel(model: MedicinePlanItem): MedicinePlanItemData {
            return MedicinePlanItemData(
                medicinePlanId = model.medicinePlanId,
                medicineName = model.medicineName,
                medicineUnit = model.medicineUnit,
                planDuration = getDuration(model)
            )
        }

        private fun getDuration(model: MedicinePlanItem): String {
            return when (model.planType) {
                MedicinePlan.Type.ONCE -> getDurationForOnce(model.startDate)
                MedicinePlan.Type.PERIOD -> getDurationForPerion(model.startDate, model.endDate!!)
                MedicinePlan.Type.CONTINUOUS -> getDurationForContinuous(model.startDate)
            }
        }

        private fun getDurationForOnce(date: AppDate): String {
            return "Jednorazowo ${date.formatString}"
        }

        private fun getDurationForPerion(startDate: AppDate, endDate: AppDate): String {
            return "Od ${startDate.formatString} to ${endDate.formatString}"
        }

        private fun getDurationForContinuous(startDate: AppDate): String {
            return "Przyjmowanie ciągłe od ${startDate.formatString}"
        }
    }
}