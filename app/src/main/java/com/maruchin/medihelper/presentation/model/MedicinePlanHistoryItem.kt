package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.PlannedMedicine

data class MedicinePlanHistoryItem(
    val plannedDate: AppDate,
    val isToday: Boolean
) {
    constructor(
        plannedDate: AppDate,
        plannedMedicineList: List<PlannedMedicine>,
        currDate: AppDate
    ) : this(
        plannedDate = plannedDate,
        isToday = plannedDate == currDate
    )
}