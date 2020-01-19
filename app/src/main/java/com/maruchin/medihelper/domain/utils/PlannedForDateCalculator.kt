package com.maruchin.medihelper.domain.utils

import com.maruchin.medihelper.domain.entities.AppDate
import com.maruchin.medihelper.domain.entities.IntakeDays
import com.maruchin.medihelper.domain.entities.Plan

class PlannedForDateCalculator(
    private val plan: Plan,
    private val selectedDate: AppDate
) {
    fun isPlannedForDate(): Boolean {
        return when (plan.planType) {
            Plan.Type.ONE_DAY -> isOneDayPlannedForDate()
            Plan.Type.PERIOD -> isPeriodPlannedForDate()
            Plan.Type.CONTINUOUS -> isContinuousPlannedForDate()
        }
    }

    private fun isOneDayPlannedForDate(): Boolean {
        return selectedDate == plan.startDate
    }

    private fun isPeriodPlannedForDate(): Boolean {
        return when {
            selectedDate < plan.startDate -> false
            selectedDate > plan.endDate!! -> false
            else -> isPlannedForDateByIntakeDays()
        }
    }

    private fun isContinuousPlannedForDate(): Boolean {
        return when {
            selectedDate < plan.startDate -> false
            else -> isPlannedForDateByIntakeDays()
        }
    }

    private fun isPlannedForDateByIntakeDays(): Boolean {
        return when (plan.intakeDays!!) {
            is IntakeDays.Everyday -> true
            is IntakeDays.DaysOfWeek -> isPlannedForDateByDaysOfWeek()
            is IntakeDays.Interval -> isPlannedForDateByInterval()
            is IntakeDays.Sequence -> isPlannedForDateBySequence()
        }
    }

    private fun isPlannedForDateByDaysOfWeek(): Boolean {
        val dayOfWeek = selectedDate.dayOfWeek
        val intakeDays = plan.intakeDays as IntakeDays.DaysOfWeek
        return intakeDays.isDaySelected(dayOfWeek)
    }

    private fun isPlannedForDateByInterval(): Boolean {
        val intakeDays = plan.intakeDays as IntakeDays.Interval
        val interval = intakeDays.daysCount
        val daysDiff = selectedDate - plan.startDate
        val rem = daysDiff % interval
        return rem == 0
    }

    private fun isPlannedForDateBySequence(): Boolean {
        val intakeDays = plan.intakeDays as IntakeDays.Sequence
        val intakeCount = intakeDays.intakeCount
        val fullSequenceCount = intakeCount + intakeDays.notIntakeCount
        val daysDiff = selectedDate - plan.startDate
        val rem = daysDiff % fullSequenceCount
        return rem < intakeCount
    }
}