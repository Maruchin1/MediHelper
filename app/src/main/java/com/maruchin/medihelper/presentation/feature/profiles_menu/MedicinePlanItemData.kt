package com.maruchin.medihelper.presentation.feature.profiles_menu

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.model.PlanItem

data class MedicinePlanItemData(
    val medicinePlanId: String,
    val medicineName: String,
    val medicineUnit: String,
    val planDuration: String
) {

    companion object {

        fun fromDomainModel(model: PlanItem): MedicinePlanItemData {
            return MedicinePlanItemData(
                medicinePlanId = model.medicinePlanId,
                medicineName = model.medicineName,
                medicineUnit = model.medicineUnit,
                planDuration = getDuration(
                    model
                )
            )
        }

        private fun getDuration(model: PlanItem): String {
            return when (model.planType) {
                Plan.Type.ONE_DAY -> getDurationForOnce(
                    model.startDate
                )
                Plan.Type.PERIOD -> getDurationForPerion(
                    model.startDate,
                    model.endDate!!
                )
                Plan.Type.CONTINUOUS -> getDurationForContinuous(
                    model.startDate
                )
            }
        }

        private fun getDurationForOnce(date: AppDate): String {
            return "Jednego dnia ${date.formatString}"
        }

        private fun getDurationForPerion(startDate: AppDate, endDate: AppDate): String {
            return "Od ${startDate.formatString} to ${endDate.formatString}"
        }

        private fun getDurationForContinuous(startDate: AppDate): String {
            return "Przyjmowanie ciągłe od ${startDate.formatString}"
        }
    }
}