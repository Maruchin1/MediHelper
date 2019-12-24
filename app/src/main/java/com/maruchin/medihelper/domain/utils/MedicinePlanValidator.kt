package com.maruchin.medihelper.domain.utils

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.IntakeDays
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.entities.TimeDose

class MedicinePlanValidator{

    fun validate(params: Params): Errors {
        val errors = Errors()

        if (params.profileId.isNullOrEmpty()) {
            errors.emptyProfileId = true
        }
        if (params.medicineId.isNullOrEmpty()) {
            errors.emptyMedicineId = true
        }
        if (params.planType == null) {
            errors.emptyPlanType = true
        }
        when (params.planType) {
            null -> errors.emptyPlanType = true
            MedicinePlan.Type.ONCE -> {
                if (params.startDate == null) {
                    errors.emptyStartDate = true
                }
            }
            MedicinePlan.Type.PERIOD -> {
                if (params.startDate == null) {
                    errors.emptyStartDate = true
                }
                if (params.endDate == null) {
                    errors.emptyEndDate = true
                }
                if (params.startDate != null &&
                    params.endDate != null &&
                    params.startDate > params.endDate
                ) {
                    errors.incorrectDatesOrder = true
                }
                if (params.intakeDays == null) {
                    errors.emptyIntakeDays = true
                }
            }
            MedicinePlan.Type.CONTINUOUS -> {
                if (params.startDate == null) {
                    errors.emptyStartDate = true
                }
                if (params.intakeDays == null) {
                    errors.emptyIntakeDays = true
                }
            }
        }
        if (params.timeDoseList.isNullOrEmpty()) {
            errors.emptyTimeDoseList = true
        }
        return errors
    }

    data class Params(
        val profileId: String?,
        val medicineId: String?,
        val planType: MedicinePlan.Type?,
        val startDate: AppDate?,
        val endDate: AppDate?,
        val intakeDays: IntakeDays?,
        val timeDoseList: List<TimeDose>?
    )

    data class Errors(
        var emptyProfileId: Boolean = false,
        var emptyMedicineId: Boolean = false,
        var emptyPlanType: Boolean = false,
        var emptyStartDate: Boolean = false,
        var emptyEndDate: Boolean = false,
        var incorrectDatesOrder: Boolean = false,
        var emptyIntakeDays: Boolean = false,
        var emptyTimeDoseList: Boolean = false
    ) {
        val noErrors: Boolean
            get() = arrayOf(
                emptyProfileId,
                emptyMedicineId,
                emptyPlanType,
                emptyStartDate,
                emptyEndDate,
                incorrectDatesOrder,
                emptyIntakeDays,
                emptyTimeDoseList
            ).all { !it }
    }
}