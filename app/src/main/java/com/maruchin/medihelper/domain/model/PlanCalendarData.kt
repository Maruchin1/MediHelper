package com.maruchin.medihelper.domain.model

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.Medicine
import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlanCalendarData(
    private val plan: Plan,
    private val medicine: Medicine
) {

    suspend fun getPlannedMedicinesItemsForDate(date: AppDate): List<PlannedMedicineItem> =
        withContext(Dispatchers.Default) {
            val plannedMedicines = plan.getPlannedMedicinesForDate(date)
            return@withContext mapToItems(plannedMedicines)
        }

    private fun mapToItems(entities: List<PlannedMedicine>): List<PlannedMedicineItem> {
        return entities.map { plannedMedicine ->
            PlannedMedicineItem(plannedMedicine, medicine)
        }
    }
}