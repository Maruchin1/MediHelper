package com.maruchin.medihelper.domain.usecasesimpl.mediplans

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.HistoryItem
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.mediplans.GetMedicinePlanHistoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetMedicinePlanHistoryUseCaseImpl(
    private val plannedMedicineRepo: PlannedMedicineRepo
) : GetMedicinePlanHistoryUseCase {

    override suspend fun execute(medicinePlanId: String): List<HistoryItem> = withContext(Dispatchers.Default) {
        val plannedMedicines = plannedMedicineRepo.getListByMedicinePlan(medicinePlanId)
        val plannedMedicinesGroupedByDate = groupByDate(plannedMedicines)
        val historyItems = mapGroupedPlannedMedicinesToHistoryItems(plannedMedicinesGroupedByDate)
        val historyItemsSortedByDate = sortHistoryItemsByDate(historyItems)
        return@withContext historyItemsSortedByDate
    }

    private fun groupByDate(plannedMedicines: List<PlannedMedicine>): Map<AppDate, List<PlannedMedicine>> {
        return plannedMedicines.groupBy { plannedMedicine ->
            plannedMedicine.plannedDate
        }
    }

    private fun mapGroupedPlannedMedicinesToHistoryItems(
        groupedPlannedMedicines: Map<AppDate, List<PlannedMedicine>>
    ): List<HistoryItem> {
        return groupedPlannedMedicines.map { dateAndPlannedMedicines ->
            getHistoryItem(
                date = dateAndPlannedMedicines.key,
                plannedMedicines = dateAndPlannedMedicines.value
            )
        }
    }

    private fun sortHistoryItemsByDate(historyItems: List<HistoryItem>): List<HistoryItem> {
        return historyItems.sortedBy { item ->
            item.date
        }
    }

    private fun getHistoryItem(date: AppDate, plannedMedicines: List<PlannedMedicine>): HistoryItem {
        val checkboxes = mapPlannedMedicinesToCheckboxes(plannedMedicines)
        val checkboxesSortedByTime = sortCheckboxesByTime(checkboxes)
        return HistoryItem(
            date = date,
            checkboxesList = checkboxesSortedByTime
        )
    }

    private fun mapPlannedMedicinesToCheckboxes(plannedMedicines: List<PlannedMedicine>): List<HistoryItem.CheckBox> {
        return plannedMedicines.map { plannedMedicine ->
            getHistoryCheckbox(plannedMedicine)
        }
    }

    private fun sortCheckboxesByTime(checkboxes: List<HistoryItem.CheckBox>): List<HistoryItem.CheckBox> {
        return checkboxes.sortedBy { checkbox ->
            checkbox.plannedTime
        }
    }

    private fun getHistoryCheckbox(plannedMedicine: PlannedMedicine): HistoryItem.CheckBox {
        return HistoryItem.CheckBox(
            plannedMedicineId = plannedMedicine.entityId,
            plannedTime = plannedMedicine.plannedTime,
            status = plannedMedicine.status
        )
    }
}