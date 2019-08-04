package com.example.medihelper.localdatabase

import com.example.medihelper.AppDateTimeUtil
import com.example.medihelper.localdatabase.entities.MedicinePlan
import com.example.medihelper.localdatabase.entities.PlannedMedicine
import java.util.*
import kotlin.collections.ArrayList

class PlannedMedicineScheduler {

    fun getPlannedMedicineList(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        return when (medicinePlan.durationType) {
            MedicinePlan.DurationType.ONCE -> getForNone(medicinePlan)
            MedicinePlan.DurationType.PERIOD -> {
                when (medicinePlan.daysType) {
                    MedicinePlan.DaysType.EVERYDAY -> getForPeriodEveryday(medicinePlan)
                    MedicinePlan.DaysType.DAYS_OF_WEEK -> getForPeriodDaysOfWeek(medicinePlan)
                    MedicinePlan.DaysType.INTERVAL_OF_DAYS -> getForPeriodIntervalOfDays(medicinePlan)
                    else -> emptyList()
                }
            }
            MedicinePlan.DurationType.CONTINUOUS -> {
                when (medicinePlan.daysType) {
                    MedicinePlan.DaysType.EVERYDAY -> getForContinuousEveryday(medicinePlan)
                    MedicinePlan.DaysType.DAYS_OF_WEEK -> getForContinuousDaysOfWeek(medicinePlan)
                    MedicinePlan.DaysType.INTERVAL_OF_DAYS -> getForContinuousIntervalOfDays(medicinePlan)
                    else -> emptyList()
                }
            }
        }
    }

    private fun getForNone(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicine>()
        plannedMedicineArrayList.addAll(
            getForDate(
                medicinePlanID = medicinePlan.medicinePlanID,
                date = medicinePlan.startDate,
                timeOfTakingList = medicinePlan.timeOfTakingList
            )
        )
        return plannedMedicineArrayList
    }

    private fun getForPeriodEveryday(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicine>()
        val currCalendar = AppDateTimeUtil.getCurrCalendar().apply {
            time = medicinePlan.startDate
        }
        while (AppDateTimeUtil.compareDates(currCalendar.time, medicinePlan.endDate!!) != 1) {
            plannedMedicineArrayList.addAll(
                getForDate(
                    medicinePlanID = medicinePlan.medicinePlanID,
                    date = currCalendar.time,
                    timeOfTakingList = medicinePlan.timeOfTakingList
                )
            )
            currCalendar.add(Calendar.DATE, 1)
        }
        return plannedMedicineArrayList
    }

    private fun getForPeriodDaysOfWeek(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicine>()
        val currCalendar = AppDateTimeUtil.getCurrCalendar().apply {
            time = medicinePlan.startDate
        }
        while (AppDateTimeUtil.compareDates(currCalendar.time, medicinePlan.endDate!!) != 1) {
            val currDayOfWeekNumber = currCalendar.get(Calendar.DAY_OF_WEEK)
            if (medicinePlan.daysOfWeek?.isDaySelected(currDayOfWeekNumber) == true) {
                plannedMedicineArrayList.addAll(
                    getForDate(
                        medicinePlanID = medicinePlan.medicinePlanID,
                        date = currCalendar.time,
                        timeOfTakingList = medicinePlan.timeOfTakingList
                    )
                )
            }
            currCalendar.add(Calendar.DATE, 1)
        }
        return plannedMedicineArrayList
    }

    private fun getForPeriodIntervalOfDays(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicine>()
        val currCalendar = AppDateTimeUtil.getCurrCalendar().apply {
            time = medicinePlan.startDate
        }
        while (AppDateTimeUtil.compareDates(currCalendar.time, medicinePlan.endDate!!) != 1) {
            plannedMedicineArrayList.addAll(
                getForDate(
                    medicinePlanID = medicinePlan.medicinePlanID,
                    date = currCalendar.time,
                    timeOfTakingList = medicinePlan.timeOfTakingList
                )
            )
            currCalendar.add(Calendar.DATE, medicinePlan.intervalOfDays!!)
        }
        return plannedMedicineArrayList
    }

    private fun getForContinuousEveryday(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        return emptyList()
    }

    private fun getForContinuousDaysOfWeek(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        return emptyList()
    }

    private fun getForContinuousIntervalOfDays(medicinePlan: MedicinePlan): List<PlannedMedicine> {
        return emptyList()
    }

    private fun getForDate(medicinePlanID: Int, date: Date, timeOfTakingList: List<MedicinePlan.TimeOfTaking>): List<PlannedMedicine> {
        val plannedMedicineArrayList = ArrayList<PlannedMedicine>()
        timeOfTakingList.forEach { timeOfTaking ->
            plannedMedicineArrayList.add(
                PlannedMedicine(
                    medicinePlanID = medicinePlanID,
                    plannedDate = date,
                    plannedTime = timeOfTaking.time,
                    plannedDoseSize = timeOfTaking.doseSize
                )
            )
        }
        return plannedMedicineArrayList
    }
}