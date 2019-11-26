package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.PlannedMedicine

data class MedicinePlanHistoryItem(
    val plannedDate: AppDate,
    val historyCheckboxList: List<MedicinePlanHistoryCheckbox>,
    val isToday: Boolean
) {
    constructor(
        plannedDate: AppDate,
        plannedMedicineList: List<PlannedMedicine>,
        currDate: AppDate
    ) : this(
        plannedDate = plannedDate,
        historyCheckboxList = plannedMedicineList.map { MedicinePlanHistoryCheckbox(it) },
        isToday = plannedDate == currDate
    )
}