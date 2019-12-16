package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.TimeDose

class MedicinePlanOnceValidator(
    profileId: String?,
    medicineId: String?,
    date: AppDate?,
    timeDoseList: List<TimeDose>?
) {
    val noErrors: Boolean
        get() = arrayOf(
            emptyProfileId,
            emptyMedicineId,
            emptyDate,
            emptyTimeDoseList
        ).all { !it }
    var emptyProfileId: Boolean = false
        private set
    var emptyMedicineId: Boolean = false
        private set
    var emptyDate: Boolean = false
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
        if (date == null) {
            emptyDate = true
        }
        if (timeDoseList.isNullOrEmpty()) {
            emptyTimeDoseList = true
        }
    }
}