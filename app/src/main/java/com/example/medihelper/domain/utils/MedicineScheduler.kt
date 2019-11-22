package com.example.medihelper.domain.utils

import com.example.medihelper.domain.entities.*
import kotlin.collections.ArrayList

class MedicineScheduler {

    fun getPlannedMedicinesForMedicinePlan(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        return when (medicinePlan.durationType) {
            DurationType.ONCE -> getForDate(
                medicinePlanId = medicinePlan.medicinePlanId,
                plannedDate = medicinePlan.startDate,
                timeDoseList = medicinePlan.timeDoseList
            )
            DurationType.PERIOD -> {
                when (medicinePlan.daysType) {
                    DaysType.EVERYDAY -> getForEveryday(medicinePlan)
                    DaysType.DAYS_OF_WEEK -> getForDaysOfWeek(medicinePlan)
                    DaysType.INTERVAL_OF_DAYS -> getForIntervalOfDays(medicinePlan)
                    else -> emptyList()
                }
            }
            DurationType.CONTINUOUS -> {
                val tempMedicinePlan = medicinePlan.copy(
                    endDate = medicinePlan.startDate.copy().apply { addDays(CONTINUOUS_DAYS_COUNT) }
                )
                when (medicinePlan.daysType) {
                    DaysType.EVERYDAY -> getForEveryday(tempMedicinePlan)
                    DaysType.DAYS_OF_WEEK -> getForDaysOfWeek(tempMedicinePlan)
                    DaysType.INTERVAL_OF_DAYS -> getForIntervalOfDays(tempMedicinePlan)
                    else -> emptyList()
                }
            }
        }
    }

    private fun getForEveryday(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicine>()
        val currDate = medicinePlan.startDate.copy()

        while (currDate <= medicinePlan.endDate!!) {
            plannedMedicineArrayList.addAll(
                getForDate(
                    medicinePlanId = medicinePlan.medicinePlanId,
                    plannedDate = currDate.copy(),
                    timeDoseList = medicinePlan.timeDoseList
                )
            )
            currDate.addDays(1)
        }
        return plannedMedicineArrayList
    }

    private fun getForDaysOfWeek(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicine>()
        val currDate = medicinePlan.startDate.copy()

        while (currDate <= medicinePlan.endDate!!) {
            if (medicinePlan.daysOfWeek?.isDaySelected(currDate.dayOfWeek) == true) {
                plannedMedicineArrayList.addAll(
                    getForDate(
                        medicinePlanId = medicinePlan.medicinePlanId,
                        plannedDate = currDate.copy(),
                        timeDoseList = medicinePlan.timeDoseList
                    )
                )
            }
            currDate.addDays(1)
        }
        return plannedMedicineArrayList
    }

    private fun getForIntervalOfDays(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicine>()
        val currDate = medicinePlan.startDate.copy()

        while (currDate <= medicinePlan.endDate!!) {
            plannedMedicineArrayList.addAll(
                getForDate(
                    medicinePlanId = medicinePlan.medicinePlanId,
                    plannedDate = currDate.copy(),
                    timeDoseList = medicinePlan.timeDoseList
                )
            )
            currDate.addDays(medicinePlan.intervalOfDays!!)
        }
        return plannedMedicineArrayList
    }

    private fun getForDate(
        medicinePlanId: Int,
        plannedDate: AppDate,
        timeDoseList: List<TimeDose>
    ): List<PlannedMedicine> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicine>()
        timeDoseList.forEach { timeOfTaking ->
            plannedMedicineArrayList.add(
                PlannedMedicine(
                    plannedMedicineId = 0,
                    medicinePlanId = medicinePlanId,
                    plannedDate = plannedDate,
                    plannedTime = timeOfTaking.time,
                    plannedDoseSize = timeOfTaking.doseSize,
                    statusOfTaking = StatusOfTaking.WAITING
                )
            )
        }
        return plannedMedicineArrayList
    }

    companion object {
        private const val CONTINUOUS_DAYS_COUNT = 60
    }
}