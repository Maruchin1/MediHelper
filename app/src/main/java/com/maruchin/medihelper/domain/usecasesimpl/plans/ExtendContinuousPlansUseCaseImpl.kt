package com.maruchin.medihelper.domain.usecasesimpl.plans

import com.maruchin.medihelper.domain.device.DeviceCalendar
import com.maruchin.medihelper.domain.entities.Plan
import com.maruchin.medihelper.domain.repositories.PlanRepo
import com.maruchin.medihelper.domain.repositories.PlannedMedicineRepo
import com.maruchin.medihelper.domain.usecases.plans.ExtendContinuousPlansUseCase
import com.maruchin.medihelper.domain.utils.PlannedMedicineScheduler

class ExtendContinuousPlansUseCaseImpl(
    private val planRepo: PlanRepo,
    private val plannedMedicineRepo: PlannedMedicineRepo,
    private val deviceCalendar: DeviceCalendar,
    private val plannedMedicineScheduler: PlannedMedicineScheduler
) : ExtendContinuousPlansUseCase {

    companion object {
        private const val DAYS_TO_END_WHEN_UPDATE = 7
        private const val CONTINUOUS_TEMP_PERIOD_DAYS = 30
    }

    override suspend fun execute() {
        val continuousPlan = planRepo.getListByType(Plan.Type.CONTINUOUS)
        val closeToEnd = findPlansCloseToTempEnd(continuousPlan)
        for (singlePlan in closeToEnd) {
            extendPlan(singlePlan)
        }
    }

    private fun findPlansCloseToTempEnd(plans: List<Plan>): List<Plan> {
        val currDate = deviceCalendar.getCurrDate()
        return plans.filter { singlePlan ->
            val daysToEnd = singlePlan.endDate!! - currDate
            daysToEnd < DAYS_TO_END_WHEN_UPDATE
        }
    }

    private suspend fun extendPlan(plan: Plan) {
        addPlannedMedicinesForNextPeriod(plan)
        val updatedPlan = plan.copy(
            endDate = plan.endDate!!.copy().apply { addDays(CONTINUOUS_TEMP_PERIOD_DAYS) }
        )
        planRepo.update(updatedPlan)
    }

    private suspend fun addPlannedMedicinesForNextPeriod(plan: Plan) {
        val nextPeriodPlan = plan.copy(
            startDate = plan.endDate!!,
            endDate = plan.endDate.copy().apply { addDays(CONTINUOUS_TEMP_PERIOD_DAYS) }
        )
        val nextPlannedMedicines = plannedMedicineScheduler.getPlannedMedicines(nextPeriodPlan)
        plannedMedicineRepo.addNewList(nextPlannedMedicines)
    }
}