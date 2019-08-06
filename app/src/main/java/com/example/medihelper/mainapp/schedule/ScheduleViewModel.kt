package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.ViewModel
import com.example.medihelper.AppDateTimeUtil
import com.example.medihelper.AppRepository
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicinePlan
import com.example.medihelper.localdatabase.entities.PlannedMedicine
import java.util.*

class ScheduleViewModel : ViewModel() {

    val timelineDaysCount = 10000
    val initialDatePosition = timelineDaysCount / 2

    val medicineListLive = AppRepository.getMedicineListLive()
    val medicineTypeListLive = AppRepository.getMedicineTypeListLive()
    val medicinePlanListLive = AppRepository.getMedicinesPlanListLive()

    fun getDateForPosition(position: Int): Date {
        val calendar = AppDateTimeUtil.getCurrCalendar()
        calendar.add(Calendar.DAY_OF_YEAR, position - (timelineDaysCount / 2))
        return calendar.time
    }

    fun getPlannedMedicinesForDateListLive(date: Date) = AppRepository.getPlannedMedicineListByDateLive(date)

    fun getPlannedMedicineDisplayData(plannedMedicine: PlannedMedicine): PlannedMedicineDisplayData {
        val medicinePlan = getMedicinePlanById(plannedMedicine.medicinePlanID)
        val medicine = getMedicineById(medicinePlan?.medicineID)
        val medicineType = getMedicineTypeById(medicine?.medicineTypeID)
        return PlannedMedicineDisplayData(
            medicineName = medicine?.name ?: "--",
            doseSize = "Dawka: ${plannedMedicine.plannedDoseSize} ${medicineType?.typeName ?: "--"}",
            time = AppDateTimeUtil.timeToString(plannedMedicine.plannedTime),
            statusOfTaking = when (plannedMedicine.statusOfTaking) {
                PlannedMedicine.StatusOfTaking.WAITING -> "oczekujący"
                PlannedMedicine.StatusOfTaking.TAKEN -> "przyjety"
                PlannedMedicine.StatusOfTaking.NOT_TAKEN -> "nieprzyjęty"
            },
            statusOfTakingColorId = when (plannedMedicine.statusOfTaking) {
                PlannedMedicine.StatusOfTaking.WAITING -> R.color.colorDarkerGray
                PlannedMedicine.StatusOfTaking.TAKEN -> R.color.colorStateGood
                PlannedMedicine.StatusOfTaking.NOT_TAKEN -> R.color.colorStateSmall
            }
        )
    }

    fun getMedicinePlanDisplayData(medicinePlan: MedicinePlan): MedicinePlanDisplayData {
        val medicine = getMedicineById(medicinePlan.medicineID)
        val medicineType = getMedicineTypeById(medicine?.medicineTypeID)
        return MedicinePlanDisplayData(
            medicinePlanRef = medicinePlan,
            medicineName = medicine?.name ?: "--",
            durationType = when (medicinePlan.durationType) {
                MedicinePlan.DurationType.ONCE -> "Jednorazowo"
                MedicinePlan.DurationType.PERIOD -> "Przez ${AppDateTimeUtil.daysBetween(medicinePlan.startDate, medicinePlan.endDate!!)} dni"
                MedicinePlan.DurationType.CONTINUOUS -> "Leczenie ciągłe"
            },
            startDate = when (medicinePlan.durationType) {
                MedicinePlan.DurationType.ONCE -> AppDateTimeUtil.dateToString(medicinePlan.startDate)
                else -> "Od ${AppDateTimeUtil.dateToString(medicinePlan.startDate)}"
            },
            endDate = when (medicinePlan.durationType) {
                MedicinePlan.DurationType.PERIOD -> "Do ${AppDateTimeUtil.dateToString(medicinePlan.endDate!!)}"
                else -> ""
            },
            daysType = when (medicinePlan.daysType) {
                MedicinePlan.DaysType.NONE -> ""
                MedicinePlan.DaysType.EVERYDAY -> "Codziennie"
                MedicinePlan.DaysType.DAYS_OF_WEEK -> medicinePlan.daysOfWeek?.getSelectedDaysString() ?: "--"
                MedicinePlan.DaysType.INTERVAL_OF_DAYS -> "Co ${medicinePlan.intervalOfDays ?: "--"} dni"
            },
            timeOfTaking = StringBuilder().run {
                medicinePlan.timeOfTakingList.forEach { timeOfTaking ->
                    append(AppDateTimeUtil.timeToString(timeOfTaking.time))
                    append(" - ")
                    append(timeOfTaking.doseSize)
                    append(" ")
                    append(medicineType?.typeName ?: "brak typu")
                    append("\n")
                }
                toString()
            }
        )
    }

    fun deleteMedicinePlan(medicinePlan: MedicinePlan) = AppRepository.deleteMedicinePlan(medicinePlan)

    private fun getMedicineById(medicineID: Int?) = medicineListLive.value?.find { medicine ->
        medicine.medicineID == medicineID
    }

    private fun getMedicineTypeById(medicineTypeID: Int?) = medicineTypeListLive.value?.find { medicineType ->
        medicineType.medicineTypeID == medicineTypeID
    }

    private fun getMedicinePlanById(medicinePlanID: Int?) = medicinePlanListLive.value?.find { medicinePlan ->
        medicinePlan.medicinePlanID == medicinePlanID
    }

    data class PlannedMedicineDisplayData(
        val medicineName: String,
        val doseSize: String,
        val time: String,
        val statusOfTaking: String,
        val statusOfTakingColorId: Int
    )

    data class MedicinePlanDisplayData(
        val medicinePlanRef: MedicinePlan,
        val medicineName: String,
        val durationType: String,
        val startDate: String,
        val endDate: String,
        val daysType: String,
        val timeOfTaking: String
    )
}