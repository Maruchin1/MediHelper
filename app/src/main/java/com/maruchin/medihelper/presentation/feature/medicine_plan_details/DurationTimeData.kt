package com.maruchin.medihelper.presentation.feature.medicine_plan_details

import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.model.MedicinePlanDetails

data class DurationTimeData(
    val planType: String,
    val startDate: String,
    val endDate: String?
) {

    companion object {

        fun fromDomainModel(model: MedicinePlanDetails): DurationTimeData {
            return when (model.planType) {
                MedicinePlan.Type.ONCE -> getForOnce(
                    model
                )
                MedicinePlan.Type.PERIOD -> getForPeriod(
                    model
                )
                MedicinePlan.Type.CONTINUOUS -> getForContinuous(
                    model
                )
            }
        }

        private fun getForOnce(model: MedicinePlanDetails) =
            DurationTimeData(
                planType = "Jednorazowo",
                startDate = model.startDate.formatString,
                endDate = null
            )

        private fun getForPeriod(model: MedicinePlanDetails) =
            DurationTimeData(
                planType = "Przez okres dni",
                startDate = "Od ${model.startDate.formatString}",
                endDate = "Do ${model.endDate!!.formatString}"
            )

        private fun getForContinuous(model: MedicinePlanDetails) =
            DurationTimeData(
                planType = "Przyjmowanie ciągłe",
                startDate = "Od ${model.startDate.formatString}",
                endDate = null
            )
    }
}