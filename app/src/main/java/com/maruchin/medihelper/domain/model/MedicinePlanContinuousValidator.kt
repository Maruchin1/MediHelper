package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.IntakeDays
import com.maruchin.medihelper.domain.entities.TimeDose

class MedicinePlanContinuousValidator(
    profileId: String?,
    medicineId: String?,
    startDate: AppDate?,
    intakeDays: IntakeDays?,
    timeDoseList: List<TimeDose>?
) {
    val noErrors: Boolean
        get() = arrayOf(
            emptyProfileId,
            emptyMedicineId,
            emptyStartDate,
            emptyIntakeDays,
            emptyTimeDoseList
        ).all { !it }
    var emptyProfileId: Boolean = false
        private set
    var emptyMedicineId: Boolean = false
        private set
    var emptyStartDate: Boolean = false
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
        if (startDate == null) {
            emptyStartDate = true
        }
        if (intakeDays == null) {
            emptyIntakeDays = true
        }
        if (timeDoseList.isNullOrEmpty()) {
            emptyTimeDoseList = true
        }
    }
}