package com.example.medihelper.services

import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
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
        updatePlannedMedicinesStatuses()
    }

    suspend fun updatePlannedMedicines(medicinePlanID: Int) {
        val currDate = AppDate.currDate()
        plannedMedicineRepository.deleteFromDateByMedicinePlanID(currDate, medicinePlanID)
        val medicinePlanEntity = medicinePlanRepository.getEntity(medicinePlanID)
        val updatedPartOfMedicinePlan = medicinePlanEntity.copy(startDate = currDate)
        val plannedMedicineList = getPlannedMedicineList(updatedPartOfMedicinePlan)
        plannedMedicineRepository.insert(plannedMedicineList)
        updatePlannedMedicinesStatuses()
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
                val tempMedicinePlan = medicinePlanEntity.copy(
                    endDate = medicinePlanEntity.startDate.copy().apply { addDays(CONTINUOUS_DAYS_COUNT) }
                )
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
        val currDate = medicinePlanEntity.startDate.copy()

        while (currDate <= medicinePlanEntity.endDate!!) {
            plannedMedicineArrayList.addAll(
                getForDate(
                    medicinePlanID = medicinePlanEntity.medicinePlanID,
                    plannedDate = currDate.copy(),
                    timeOfTakingList = medicinePlanEntity.timeOfTakingList
                )
            )
            currDate.addDays(1)
        }
        return plannedMedicineArrayList
    }

    private fun getForDaysOfWeek(medicinePlanEntity: MedicinePlanEntity): List<PlannedMedicineEntity> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicineEntity>()
        val currDate = medicinePlanEntity.startDate.copy()

        while (currDate <= medicinePlanEntity.endDate!!) {
            if (medicinePlanEntity.daysOfWeek?.isDaySelected(currDate.dayOfWeek) == true) {
                plannedMedicineArrayList.addAll(
                    getForDate(
                        medicinePlanID = medicinePlanEntity.medicinePlanID,
                        plannedDate = currDate.copy(),
                        timeOfTakingList = medicinePlanEntity.timeOfTakingList
                    )
                )
            }
            currDate.addDays(1)
        }
        return plannedMedicineArrayList
    }

    private fun getForIntervalOfDays(medicinePlanEntity: MedicinePlanEntity): List<PlannedMedicineEntity> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicineEntity>()
        val currDate = medicinePlanEntity.startDate.copy()

        while (currDate <= medicinePlanEntity.endDate!!) {
            plannedMedicineArrayList.addAll(
                getForDate(
                    medicinePlanID = medicinePlanEntity.medicinePlanID,
                    plannedDate = currDate.copy(),
                    timeOfTakingList = medicinePlanEntity.timeOfTakingList
                )
            )
            currDate.addDays(medicinePlanEntity.intervalOfDays!!)
        }
        return plannedMedicineArrayList
    }

    private fun getForDate(
        medicinePlanID: Int,
        plannedDate: AppDate,
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