package com.maruchin.medihelper.domain.entities

import com.maruchin.medihelper.domain.framework.BaseEntity
import com.maruchin.medihelper.domain.utils.PlannedForDateCalculator

data class Plan(
    override val entityId: String,
    val profileId: String,
    val medicineId: String,
    val planType: Type,
    val startDate: AppDate,
    val endDate: AppDate?,
    val intakeDays: IntakeDays?,
    val timeDoseList: List<TimeDose>
) : BaseEntity() {

    fun getPlannedMedicinesForDate(date: AppDate): List<PlannedMedicine> {
        return if (isPlannedForDate(date)) {
            generatePlannedMedicines(date)
        } else {
            emptyList()
        }
    }

    private fun isPlannedForDate(date: AppDate): Boolean {
        val calculator = PlannedForDateCalculator(
            plan = this,
            selectedDate = date
        )
        return calculator.isPlannedForDate()
    }

    private fun generatePlannedMedicines(date: AppDate): List<PlannedMedicine> {
        return timeDoseList.map { singleTimeDose ->
            generateSinglePlannedMedicine(date, singleTimeDose)
        }
    }

    private fun generateSinglePlannedMedicine(date: AppDate, timeDose: TimeDose) = PlannedMedicine(
        medicinePlanId = entityId,
        profileId = profileId,
        medicineId = medicineId,
        plannedDate = date,
        plannedTime = timeDose.time,
        plannedDoseSize = timeDose.doseSize,
        status = PlannedMedicine.Status.NOT_TAKEN
    )

    enum class Type {
        ONE_DAY, PERIOD, CONTINUOUS
    }
}