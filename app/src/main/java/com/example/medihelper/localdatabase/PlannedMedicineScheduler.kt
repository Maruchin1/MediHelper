package com.example.medihelper.localdatabase

import com.example.medihelper.AppDateTimeUtil
import com.example.medihelper.localdatabase.entities.MedicinePlan
import com.example.medihelper.localdatabase.entities.PlannedMedicine
import java.util.*
import kotlin.collections.ArrayList

class PlannedMedicineScheduler {

    fun getPlannedMedicineList(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        return when (medicinePlan.durationType) {
            MedicinePlan.DurationType.ONCE -> getForDate(
                medicinePlanID = medicinePlan.medicinePlanID,
                plannedDate = medicinePlan.startDate,
                timeOfTakingList = medicinePlan.timeOfTakingList
            )
            MedicinePlan.DurationType.PERIOD -> {
                when (medicinePlan.daysType) {
                    MedicinePlan.DaysType.EVERYDAY -> getForEveryday(medicinePlan)
                    MedicinePlan.DaysType.DAYS_OF_WEEK -> getForDaysOfWeek(medicinePlan)
                    MedicinePlan.DaysType.INTERVAL_OF_DAYS -> getForIntervalOfDays(medicinePlan)
                    else -> emptyList()
                }
            }
            MedicinePlan.DurationType.CONTINUOUS -> {
                val endCalendar = AppDateTimeUtil.getCurrCalendar().apply {
                    time = medicinePlan.startDate
                    add(Calendar.DATE, CONTINUOUS_DAYS_COUNT)
                }
                val tempMedicinePlan = medicinePlan.copy(endDate = endCalendar.time)
                when (medicinePlan.daysType) {
                    MedicinePlan.DaysType.EVERYDAY -> getForEveryday(tempMedicinePlan)
                    MedicinePlan.DaysType.DAYS_OF_WEEK -> getForDaysOfWeek(tempMedicinePlan)
                    MedicinePlan.DaysType.INTERVAL_OF_DAYS -> getForIntervalOfDays(tempMedicinePlan)
                    else -> emptyList()
                }
            }
        }
    }

    private fun getForEveryday(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicine>()
        val currCalendar = AppDateTimeUtil.getCurrCalendar().apply { time = medicinePlan.startDate }

        while (AppDateTimeUtil.compareDates(currCalendar.time, medicinePlan.endDate!!) != 1) {
            plannedMedicineArrayList.addAll(
                getForDate(
                    medicinePlanID = medicinePlan.medicinePlanID,
                    plannedDate = currCalendar.time,
                    timeOfTakingList = medicinePlan.timeOfTakingList
                )
            )
            currCalendar.add(Calendar.DATE, 1)
        }
        return plannedMedicineArrayList
    }

    private fun getForDaysOfWeek(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicine>()
        val currCalendar = AppDateTimeUtil.getCurrCalendar().apply { time = medicinePlan.startDate }

        while (AppDateTimeUtil.compareDates(currCalendar.time, medicinePlan.endDate!!) != 1) {
            val currDayOfWeekNumber = currCalendar.get(Calendar.DAY_OF_WEEK)
            if (medicinePlan.daysOfWeek?.isDaySelected(currDayOfWeekNumber) == true) {
                plannedMedicineArrayList.addAll(
                    getForDate(
                        medicinePlanID = medicinePlan.medicinePlanID,
                        plannedDate = currCalendar.time,
                        timeOfTakingList = medicinePlan.timeOfTakingList
                    )
                )
            }
            currCalendar.add(Calendar.DATE, 1)
        }
        return plannedMedicineArrayList
    }

    private fun getForIntervalOfDays(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicine>()
        val currCalendar = AppDateTimeUtil.getCurrCalendar().apply { time = medicinePlan.startDate }

        while (AppDateTimeUtil.compareDates(currCalendar.time, medicinePlan.endDate!!) != 1) {
            plannedMedicineArrayList.addAll(
                getForDate(
                    medicinePlanID = medicinePlan.medicinePlanID,
                    plannedDate = currCalendar.time,
                    timeOfTakingList = medicinePlan.timeOfTakingList
                )
            )
            currCalendar.add(Calendar.DATE, medicinePlan.intervalOfDays!!)
        }
        return plannedMedicineArrayList
    }

    private fun getForDate(
        medicinePlanID: Int,
        plannedDate: Date,
        timeOfTakingList: List<MedicinePlan.TimeOfTaking>
    ): List<PlannedMedicine> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicine>()
        timeOfTakingList.forEach { timeOfTaking ->
            plannedMedicineArrayList.add(
                PlannedMedicine(
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