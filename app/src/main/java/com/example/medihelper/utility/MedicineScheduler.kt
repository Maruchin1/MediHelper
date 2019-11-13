package com.example.medihelper.utility

import com.example.medihelper.localdata.entity.PlannedMedicineEntity
import com.example.medihelper.localdata.pojo.MedicinePlanEditData
import com.example.medihelper.localdata.pojo.TimeDoseEditData
import com.example.medihelper.localdata.type.AppDate
import com.example.medihelper.localdata.type.DaysType
import com.example.medihelper.localdata.type.DurationType
import kotlin.collections.ArrayList

class MedicineScheduler {

    fun getPlannedMedicinesForMedicinePlan(editData: MedicinePlanEditData): List<PlannedMedicineEntity> {
        return when (editData.durationType) {
            DurationType.ONCE -> getForDate(
                medicinePlanID = editData.medicinePlanId,
                plannedDate = editData.startDate,
                timeDoseList = editData.timeDoseList
            )
            DurationType.PERIOD -> {
                when (editData.daysType) {
                    DaysType.EVERYDAY -> getForEveryday(editData)
                    DaysType.DAYS_OF_WEEK -> getForDaysOfWeek(editData)
                    DaysType.INTERVAL_OF_DAYS -> getForIntervalOfDays(editData)
                    else -> emptyList()
                }
            }
            DurationType.CONTINUOUS -> {
                val tempMedicinePlan = editData.copy(
                    endDate = editData.startDate.copy().apply { addDays(CONTINUOUS_DAYS_COUNT) }
                )
                when (editData.daysType) {
                    DaysType.EVERYDAY -> getForEveryday(tempMedicinePlan)
                    DaysType.DAYS_OF_WEEK -> getForDaysOfWeek(tempMedicinePlan)
                    DaysType.INTERVAL_OF_DAYS -> getForIntervalOfDays(tempMedicinePlan)
                    else -> emptyList()
                }
            }
        }
    }

    private fun getForEveryday(editData: MedicinePlanEditData): List<PlannedMedicineEntity> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicineEntity>()
        val currDate = editData.startDate.copy()

        while (currDate <= editData.endDate!!) {
            plannedMedicineArrayList.addAll(
                getForDate(
                    medicinePlanID = editData.medicinePlanId,
                    plannedDate = currDate.copy(),
                    timeDoseList = editData.timeDoseList
                )
            )
            currDate.addDays(1)
        }
        return plannedMedicineArrayList
    }

    private fun getForDaysOfWeek(editData: MedicinePlanEditData): List<PlannedMedicineEntity> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicineEntity>()
        val currDate = editData.startDate.copy()

        while (currDate <= editData.endDate!!) {
            if (editData.daysOfWeek?.isDaySelected(currDate.dayOfWeek) == true) {
                plannedMedicineArrayList.addAll(
                    getForDate(
                        medicinePlanID = editData.medicinePlanId,
                        plannedDate = currDate.copy(),
                        timeDoseList = editData.timeDoseList
                    )
                )
            }
            currDate.addDays(1)
        }
        return plannedMedicineArrayList
    }

    private fun getForIntervalOfDays(editData: MedicinePlanEditData): List<PlannedMedicineEntity> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicineEntity>()
        val currDate = editData.startDate.copy()

        while (currDate <= editData.endDate!!) {
            plannedMedicineArrayList.addAll(
                getForDate(
                    medicinePlanID = editData.medicinePlanId,
                    plannedDate = currDate.copy(),
                    timeDoseList = editData.timeDoseList
                )
            )
            currDate.addDays(editData.intervalOfDays!!)
        }
        return plannedMedicineArrayList
    }

    private fun getForDate(
        medicinePlanID: Int,
        plannedDate: AppDate,
        timeDoseList: List<TimeDoseEditData>
    ): List<PlannedMedicineEntity> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicineEntity>()
        timeDoseList.forEach { timeOfTaking ->
            plannedMedicineArrayList.add(
                PlannedMedicineEntity(
                    medicinePlanId = medicinePlanID,
                    plannedDate = plannedDate,
                    plannedTime = timeOfTaking.time,
                    plannedDoseSize = timeOfTaking.doseSize
                )
            )
        }
        return plannedMedicineArrayList
    }

    companion object {
        private const val CONTINUOUS_DAYS_COUNT = 60
    }
}