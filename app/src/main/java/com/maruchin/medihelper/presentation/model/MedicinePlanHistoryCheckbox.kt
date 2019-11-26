package com.maruchin.medihelper.presentation.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.AppTime
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.entities.StatusOfTaking

data class MedicinePlanHistoryCheckbox(
    val plannedMedicineId: Int,
    val plannedDate: AppDate,
    val plannedTime: AppTime,
    val statusOfTaking: StatusOfTaking
) {
    constructor(plannedMedicine: PlannedMedicine) : this(
        plannedMedicineId = plannedMedicine.plannedMedicineId,
        plannedDate = plannedMedicine.plannedDate,
        plannedTime = plannedMedicine.plannedTime,
        statusOfTaking = plannedMedicine.statusOfTaking
    )
}