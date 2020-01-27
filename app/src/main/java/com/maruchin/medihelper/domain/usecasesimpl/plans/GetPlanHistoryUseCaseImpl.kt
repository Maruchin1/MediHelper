package com.maruchin.medihelper.domain.usecasesimpl.plans

import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.entities.PlannedMedicine
import com.maruchin.medihelper.domain.model.HistoryItem
import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.usecases.plans.GetPlanHistoryUseCase

class GetPlanHistoryUseCaseImpl(
    private val planRepo: PlanRepo,
    private val deviceCalendar: DeviceCalendar
) : GetPlanHistoryUseCase {

    override suspend fun execute(medicinePlanId: String): List<HistoryItem> {
        val plan = planRepo.getById(medicinePlanId)!!
        return generateHistory(plan)
    }

    private fun generateHistory(plan: Plan): List<HistoryItem> {
        val iterator = plan.startDate
        val historyEnd = deviceCalendar.getCurrDate()
        val history = mutableListOf<HistoryItem>()
        while (iterator <= historyEnd) {
            val historyItem = generateHistoryItem(plan, date = iterator.copy())
            history.add(historyItem)
            iterator.addDays(1)
        }
        return history
    }

    private fun generateHistoryItem(plan: Plan, date: AppDate): HistoryItem {
        val plannedMedicines = plan.getPlannedMedicinesForDate(date)
        val checkBoxes = plannedMedicines.map { singlePlannedMedicine ->
            getHistoryCheckBox(singlePlannedMedicine)
        }
        return HistoryItem(date, checkBoxes)
    }

    private fun getHistoryCheckBox(plannedMedicine: PlannedMedicine) = HistoryItem.CheckBox(
        plannedTime = plannedMedicine.plannedTime,
        status = plannedMedicine.status
    )
}