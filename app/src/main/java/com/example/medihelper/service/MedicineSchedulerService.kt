package com.example.medihelper.service

import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.entity.MedicinePlanEntity
import com.example.medihelper.localdatabase.entity.PlannedMedicineEntity
import com.example.medihelper.localdatabase.pojo.MedicinePlanEditData
import com.example.medihelper.localdatabase.pojo.TimeDoseEditData
import kotlin.collections.ArrayList

class MedicineSchedulerService {

    fun getPlannedMedicinesForMedicinePlan(editData: MedicinePlanEditData): List<PlannedMedicineEntity> {
        return when (editData.durationType) {
            MedicinePlanEntity.DurationType.ONCE -> getForDate(
                medicinePlanID = editData.medicinePlanID,
                plannedDate = editData.startDate,
                timeDoseList = editData.timeDoseList
            )
            MedicinePlanEntity.DurationType.PERIOD -> {
                when (editData.daysType) {
                    MedicinePlanEntity.DaysType.EVERYDAY -> getForEveryday(editData)
                    MedicinePlanEntity.DaysType.DAYS_OF_WEEK -> getForDaysOfWeek(editData)
                    MedicinePlanEntity.DaysType.INTERVAL_OF_DAYS -> getForIntervalOfDays(editData)
                    else -> emptyList()
                }
            }
            MedicinePlanEntity.DurationType.CONTINUOUS -> {
                val tempMedicinePlan = editData.copy(
                    endDate = editData.startDate.copy().apply { addDays(CONTINUOUS_DAYS_COUNT) }
                )
                when (editData.daysType) {
                    MedicinePlanEntity.DaysType.EVERYDAY -> getForEveryday(tempMedicinePlan)
                    MedicinePlanEntity.DaysType.DAYS_OF_WEEK -> getForDaysOfWeek(tempMedicinePlan)
                    MedicinePlanEntity.DaysType.INTERVAL_OF_DAYS -> getForIntervalOfDays(tempMedicinePlan)
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
                    medicinePlanID = editData.medicinePlanID,
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
                        medicinePlanID = editData.medicinePlanID,
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
                    medicinePlanID = editData.medicinePlanID,
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
                    medicinePlanID = medicinePlanID,
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