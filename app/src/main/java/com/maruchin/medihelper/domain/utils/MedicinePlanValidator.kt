package com.maruchin.medihelper.domain.utils

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.IntakeDays
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.entities.TimeDose
import com.maruchin.medihelper.domain.framework.BaseValidator
import com.maruchin.medihelper.domain.model.MedicinePlanErrors

class MedicinePlanValidator : BaseValidator<MedicinePlanValidator.Params, MedicinePlanErrors>() {

    override fun validate(params: Params): MedicinePlanErrors {
        val errors = MedicinePlanErrors()

        if (params.profileId.isNullOrEmpty()) {
            errors.emptyProfileId = true
        }
        if (params.medicineId.isNullOrEmpty()) {
            errors.emptyMedicineId = true
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
}