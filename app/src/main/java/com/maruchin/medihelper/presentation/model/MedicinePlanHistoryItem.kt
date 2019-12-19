package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.MedicineCalendarEntry

data class MedicinePlanHistoryItem(
    val plannedDate: AppDate,
    val isToday: Boolean
) {
    constructor(
        plannedDate: AppDate,
        medicineCalendarEntryList: List<MedicineCalendarEntry>,
        currDate: AppDate
    ) : this(
        plannedDate = plannedDate,
        isToday = plannedDate == currDate
    )
}