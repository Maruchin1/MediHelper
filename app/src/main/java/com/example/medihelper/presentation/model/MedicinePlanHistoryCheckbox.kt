package com.example.medihelper.presentation.model

import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.AppTime
import com.example.medihelper.domain.entities.PlannedMedicine
import com.example.medihelper.domain.entities.StatusOfTaking

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