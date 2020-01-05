package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.framework.BaseErrors

data class PlanErrors(
    var emptyProfileId: Boolean = false,
    var emptyMedicineId: Boolean = false,
    var emptyPlanType: Boolean = false,
    var emptyStartDate: Boolean = false,
    var emptyEndDate: Boolean = false,
    var incorrectDatesOrder: Boolean = false,
    var emptyIntakeDays: Boolean = false,
    var emptyTimeDoseList: Boolean = false
) : BaseErrors() {

    override val noErrors: Boolean
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