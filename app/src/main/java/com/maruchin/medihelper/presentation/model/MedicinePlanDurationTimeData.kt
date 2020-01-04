package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.model.MedicinePlanDetails

data class MedicinePlanDurationTimeData(
    val planType: String,
    val startDate: String,
    val endDate: String?
) {

    companion object {

        fun fromDomainModel(model: MedicinePlanDetails): MedicinePlanDurationTimeData {
            return when (model.planType) {
                MedicinePlan.Type.ONCE -> getForOnce(model)
                MedicinePlan.Type.PERIOD -> getForPeriod(model)
                MedicinePlan.Type.CONTINUOUS -> getForContinuous(model)
            }
        }

        private fun getForOnce(model: MedicinePlanDetails) = MedicinePlanDurationTimeData(
            planType = "Jednorazowo",
            startDate = model.startDate.formatString,
            endDate = null
        )

        private fun getForPeriod(model: MedicinePlanDetails) = MedicinePlanDurationTimeData(
            planType = "Przez okres dni",
            startDate = "Od ${model.startDate.formatString}",
            endDate = "Do ${model.endDate!!.formatString}"
        )

        private fun getForContinuous(model: MedicinePlanDetails) = MedicinePlanDurationTimeData(
            planType = "Przyjmowanie ciągłe",
            startDate = "Od ${model.startDate.formatString}",
            endDate = null
        )
    }
}