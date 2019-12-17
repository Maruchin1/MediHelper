package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.IntakeDays
import com.maruchin.medihelper.domain.entities.MedicinePlan
import com.maruchin.medihelper.domain.entities.TimeDose

class MedicinePlanValidator(
    profileId: String?,
    medicineId: String?,
    planType: MedicinePlan.Type?,
    startDate: AppDate?,
    endDate: AppDate?,
    intakeDays: IntakeDays?,
    timeDoseList: List<TimeDose>?
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
    var emptyProfileId: Boolean = false
        private set
    var emptyMedicineId: Boolean = false
        private set
    var emptyPlanType: Boolean = false
        private set
    var emptyStartDate: Boolean = false
        private set
    var emptyEndDate: Boolean = false
        private set
    var incorrectDatesOrder: Boolean = false
        private set
    var emptyIntakeDays: Boolean = false
        private set
    var emptyTimeDoseList: Boolean = false
        private set

    init {
        if (profileId.isNullOrEmpty()) {
            emptyProfileId = true
        }
        if (medicineId.isNullOrEmpty()) {
            emptyMedicineId = true
        }
        if (planType == null) {
            emptyPlanType = true
        }
        when (planType) {
            null -> emptyPlanType = true
            MedicinePlan.Type.ONCE -> {
                if (startDate == null) {
                    emptyStartDate = true
                }
            }
            MedicinePlan.Type.PERIOD -> {
                if (startDate == null) {
                    emptyStartDate = true
                }
                if (endDate == null) {
                    emptyEndDate = true
                }
                if (startDate != null &&
                    endDate != null &&
                    startDate > endDate
                ) {
                    incorrectDatesOrder = true
                }
                if (intakeDays == null) {
                    emptyIntakeDays = true
                }
            }
            MedicinePlan.Type.CONTINUOUS -> {
                if (startDate == null) {
                    emptyStartDate = true
                }
                if (intakeDays == null) {
                    emptyIntakeDays = true
                }
            }
        }
        if (timeDoseList.isNullOrEmpty()) {
            emptyTimeDoseList = true
        }
    }
}