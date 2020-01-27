package com.maruchin.medihelper.presentation.feature.profiles_menu

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.model.PlanItem

data class PlanItemData(
    val medicinePlanId: String,
    val medicineName: String,
    val medicineType: String?,
    val planDuration: String,
    val active: Boolean
) {

    companion object {

        fun fromDomainModel(model: PlanItem, currDate: AppDate): PlanItemData {
            return PlanItemData(
                medicinePlanId = model.medicinePlanId,
                medicineName = model.medicineName,
                medicineType = model.medicineType,
                planDuration = getDuration(
                    model
                ),
                active = isActive(model, currDate)
            )
        }

        private fun getDuration(model: PlanItem): String {
            return when (model.planType) {
                Plan.Type.ONE_DAY -> getDurationForOnce(
                    model.startDate
                )
                Plan.Type.PERIOD -> getDurationForPeriod(
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

        private fun getDurationForPeriod(startDate: AppDate, endDate: AppDate): String {
            return "Od ${startDate.formatString} to ${endDate.formatString}"
        }

        private fun getDurationForContinuous(startDate: AppDate): String {
            return "Przyjmowanie ciągłe od ${startDate.formatString}"
        }

        private fun isActive(model: PlanItem, currDate: AppDate): Boolean {
            return when (model.planType) {
                Plan.Type.ONE_DAY -> isActiveForOneDay(model.startDate, currDate)
                Plan.Type.PERIOD -> isActiveForPeriod(model.endDate!!, currDate)
                Plan.Type.CONTINUOUS -> true
            }
        }

        private fun isActiveForOneDay(date: AppDate, currDate: AppDate): Boolean {
            return date >= currDate
        }

        private fun isActiveForPeriod(endDate: AppDate, currDate: AppDate): Boolean {
            return endDate >= currDate
        }
    }
}