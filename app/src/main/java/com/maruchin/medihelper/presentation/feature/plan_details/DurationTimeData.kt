package com.maruchin.medihelper.presentation.feature.plan_details

import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.model.PlanDetails

data class DurationTimeData(
    val planType: String,
    val startDate: String,
    val endDate: String?
) {

    companion object {

        fun fromDomainModel(model: PlanDetails): DurationTimeData {
            return when (model.planType) {
                Plan.Type.ONE_DAY -> getForOnce(
                    model
                )
                Plan.Type.PERIOD -> getForPeriod(
                    model
                )
                Plan.Type.CONTINUOUS -> getForContinuous(
                    model
                )
            }
        }

        private fun getForOnce(model: PlanDetails) =
            DurationTimeData(
                planType = "Jednego dnia",
                startDate = model.startDate.formatString,
                endDate = null
            )

        private fun getForPeriod(model: PlanDetails) =
            DurationTimeData(
                planType = "Przez okres dni",
                startDate = "Od ${model.startDate.formatString}",
                endDate = "Do ${model.endDate!!.formatString}"
            )

        private fun getForContinuous(model: PlanDetails) =
            DurationTimeData(
                planType = "Przyjmowanie ciągłe",
                startDate = "Od ${model.startDate.formatString}",
                endDate = null
            )
    }
}