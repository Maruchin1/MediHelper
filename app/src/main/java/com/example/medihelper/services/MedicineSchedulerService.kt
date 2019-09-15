package com.example.medihelper.services

import com.example.medihelper.AppDateTime
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import java.sql.Date
import java.util.*
import kotlin.collections.ArrayList

class MedicineSchedulerService(
    private val medicinePlanRepository: MedicinePlanRepository,
    private val plannedMedicineRepository: PlannedMedicineRepository
) {
    suspend fun updatePlannedMedicinesStatuses() {
        val updatedPlannedMedicineEntityList = plannedMedicineRepository.getEntityList().map { plannedMedicineEntity ->
            plannedMedicineEntity.apply {
                updateStatusByCurrDate()
            }
        }
        plannedMedicineRepository.update(updatedPlannedMedicineEntityList)
    }

    suspend fun addPlannedMedicines(medicinePlanID: Int) {
        val medicinePlanEntity = medicinePlanRepository.getEntity(medicinePlanID)
        val plannedMedicineList = getPlannedMedicineList(medicinePlanEntity)
        plannedMedicineRepository.insert(plannedMedicineList)
    }

    private fun getPlannedMedicineList(medicinePlanEntity: MedicinePlanEntity): List<PlannedMedicineEntity> {
        return when (medicinePlanEntity.durationType) {
            MedicinePlanEntity.DurationType.ONCE -> getForDate(
                medicinePlanID = medicinePlanEntity.medicinePlanID,
                plannedDate = medicinePlanEntity.startDate,
                timeOfTakingList = medicinePlanEntity.timeOfTakingList
            )
            MedicinePlanEntity.DurationType.PERIOD -> {
                when (medicinePlanEntity.daysType) {
                    MedicinePlanEntity.DaysType.EVERYDAY -> getForEveryday(medicinePlanEntity)
                    MedicinePlanEntity.DaysType.DAYS_OF_WEEK -> getForDaysOfWeek(medicinePlanEntity)
                    MedicinePlanEntity.DaysType.INTERVAL_OF_DAYS -> getForIntervalOfDays(medicinePlanEntity)
                    else -> emptyList()
                }
            }
            MedicinePlanEntity.DurationType.CONTINUOUS -> {
                val endCalendar = AppDateTime.getCurrCalendar().apply {
                    time = medicinePlanEntity.startDate
                    add(Calendar.DATE, CONTINUOUS_DAYS_COUNT)
                }
                val tempMedicinePlan = medicinePlanEntity.copy(endDate = Date(endCalendar.timeInMillis))
                when (medicinePlanEntity.daysType) {
                    MedicinePlanEntity.DaysType.EVERYDAY -> getForEveryday(tempMedicinePlan)
                    MedicinePlanEntity.DaysType.DAYS_OF_WEEK -> getForDaysOfWeek(tempMedicinePlan)
                    MedicinePlanEntity.DaysType.INTERVAL_OF_DAYS -> getForIntervalOfDays(tempMedicinePlan)
                    else -> emptyList()
                }
            }
        }
    }

    private fun getForEveryday(medicinePlanEntity: MedicinePlanEntity): List<PlannedMedicineEntity> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicineEntity>()
        val currCalendar = AppDateTime.getCurrCalendar().apply { time = medicinePlanEntity.startDate }

        while (AppDateTime.compareDates(Date(currCalendar.timeInMillis), medicinePlanEntity.endDate!!) != 1) {
            plannedMedicineArrayList.addAll(
                getForDate(
                    medicinePlanID = medicinePlanEntity.medicinePlanID,
                    plannedDate = Date(currCalendar.timeInMillis),
                    timeOfTakingList = medicinePlanEntity.timeOfTakingList
                )
            )
            currCalendar.add(Calendar.DATE, 1)
        }
        return plannedMedicineArrayList
    }

    private fun getForDaysOfWeek(medicinePlanEntity: MedicinePlanEntity): List<PlannedMedicineEntity> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicineEntity>()
        val currCalendar = AppDateTime.getCurrCalendar().apply { time = medicinePlanEntity.startDate }

        while (AppDateTime.compareDates(Date(currCalendar.timeInMillis), medicinePlanEntity.endDate!!) != 1) {
            val currDayOfWeekNumber = currCalendar.get(Calendar.DAY_OF_WEEK)
            if (medicinePlanEntity.daysOfWeek?.isDaySelected(currDayOfWeekNumber) == true) {
                plannedMedicineArrayList.addAll(
                    getForDate(
                        medicinePlanID = medicinePlanEntity.medicinePlanID,
                        plannedDate = Date(currCalendar.timeInMillis),
                        timeOfTakingList = medicinePlanEntity.timeOfTakingList
                    )
                )
            }
            currCalendar.add(Calendar.DATE, 1)
        }
        return plannedMedicineArrayList
    }

    private fun getForIntervalOfDays(medicinePlanEntity: MedicinePlanEntity): List<PlannedMedicineEntity> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicineEntity>()
        val currCalendar = AppDateTime.getCurrCalendar().apply { time = medicinePlanEntity.startDate }

        while (AppDateTime.compareDates(Date(currCalendar.timeInMillis), medicinePlanEntity.endDate!!) != 1) {
            plannedMedicineArrayList.addAll(
                getForDate(
                    medicinePlanID = medicinePlanEntity.medicinePlanID,
                    plannedDate = Date(currCalendar.timeInMillis),
                    timeOfTakingList = medicinePlanEntity.timeOfTakingList
                )
            )
            currCalendar.add(Calendar.DATE, medicinePlanEntity.intervalOfDays!!)
        }
        return plannedMedicineArrayList
    }

    private fun getForDate(
        medicinePlanID: Int,
        plannedDate: Date,
        timeOfTakingList: List<MedicinePlanEntity.TimeOfTaking>
    ): List<PlannedMedicineEntity> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicineEntity>()
        timeOfTakingList.forEach { timeOfTaking ->
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