package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppDateTimeUtil
import com.example.medihelper.AppRepository
import com.example.medihelper.R
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList

class ScheduleViewModel : ViewModel() {

    val timelineDaysCount = 10000
    val initialDatePosition = timelineDaysCount / 2

    val medicineListLive = AppRepository.getMedicinesLive()
    val medicineTypeListLive = AppRepository.getMedicineTypesLive()
    val scheduledMedicineListLive = AppRepository.getScheduledMedicinesLive()

    fun getDateForPosition(position: Int): Date {
        val calendar = AppDateTimeUtil.getCurrCalendar()
        calendar.add(Calendar.DAY_OF_YEAR, position - (timelineDaysCount / 2))
        return calendar.time
    }

    fun getScheduledMedicinesByDateLive(date: Date): LiveData<List<ScheduledMedicine>> {
        return Transformations.map(scheduledMedicineListLive) { scheduledMedicineList ->
            scheduledMedicineList.filter { scheduledMedicine ->
                scheduledMedicine.isScheduledForDate(date)
            }
        }
    }

    fun getScheduledMedicineForDayList(scheduledMedicineList: List<ScheduledMedicine>, dayDate: Date): List<ScheduledMedicineForDay> {
        val scheduledMedicineForDayList = ArrayList<ScheduledMedicineForDay>()

        scheduledMedicineList.forEach { scheduledMedicine ->
            val medicine = getMedicineById(scheduledMedicine.medicineID)
            val medicineType = medicine?.medicineTypeID?.let { getMedicineTypeById(it) }

            scheduledMedicine.timeOfTakingList.forEach { timeOfTaking ->
                val statusOfTaking = getStatusOfTaking(scheduledMedicine, dayDate, timeOfTaking.time)
                scheduledMedicineForDayList.add(
                    ScheduledMedicineForDay(
                        medicineName = medicine?.name ?: "--",
                        doseSize = "Dawka: ${timeOfTaking.doseSize} ${medicineType?.typeName ?: "--"}",
                        time = AppDateTimeUtil.timeToString(timeOfTaking.time),
                        statusOfTaking = statusOfTaking.statusString,
                        statusOfTakingColorId = statusOfTaking.statusColorId
                    )
                )
            }
        }
        return scheduledMedicineForDayList
    }

    fun getScheduledMedicineForList(scheduledMedicine: ScheduledMedicine): ScheduledMedicineForList {
        val medicine = getMedicineById(scheduledMedicine.medicineID)
        val medicineType = medicine?.medicineTypeID?.let { getMedicineTypeById(it) }
        return ScheduledMedicineForList(
            medicineName = medicine?.name ?: "--",
            durationType = when (scheduledMedicine.durationType) {
                ScheduledMedicine.DurationType.ONCE -> "Jednorazowo"
                ScheduledMedicine.DurationType.PERIOD -> "Liczba dni"
                ScheduledMedicine.DurationType.CONTINUOUS -> "Leczenie ciągłe"
            },
            durationDates = StringBuilder().run {
                append(AppDateTimeUtil.dateToString(scheduledMedicine.startDate))
                scheduledMedicine.endDate?.let { endDate ->
                    append(" - ")
                    append(AppDateTimeUtil.dateToString(endDate))
                }
                toString()
            },
            daysType = when (scheduledMedicine.daysType) {
                ScheduledMedicine.DaysType.NONE -> "--"
                ScheduledMedicine.DaysType.EVERYDAY -> "Codziennie"
                ScheduledMedicine.DaysType.DAYS_OF_WEEK -> scheduledMedicine.daysOfWeek?.getSelectedDaysString() ?: "--"
                ScheduledMedicine.DaysType.INTERVAL_OF_DAYS -> "Co ${scheduledMedicine.intervalOfDays ?: "--"} dni"
            },
            timeOfTaking = StringBuilder().run {
                scheduledMedicine.timeOfTakingList.forEach { timeOfTaking ->
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

    fun deleteScheduledMedicine(scheduledMedicine: ScheduledMedicine) = AppRepository.deleteScheduledMedicine(scheduledMedicine)

    private fun getMedicineById(medicineID: Int) = medicineListLive.value?.find { medicine ->
        medicine.medicineID == medicineID
    }

    private fun getMedicineTypeById(medicineTypeID: Int) = medicineTypeListLive.value?.find { medicineType ->
        medicineType.medicineTypeID == medicineTypeID
    }

    private fun getStatusOfTaking(scheduledMedicine: ScheduledMedicine, date: Date, time: Time): StatusOfTaking {
        val medicineTaken = scheduledMedicine.takenMedicineArrayList.any { takenMedicine ->
            val datesEqual = AppDateTimeUtil.compareDates(takenMedicine.date, date) == 0
            val timesEqual = AppDateTimeUtil.compareTimes(takenMedicine.time, time) == 0
            datesEqual && timesEqual
        }
        return if (medicineTaken) {
            StatusOfTaking.TAKEN
        } else {
            val currDate = AppDateTimeUtil.getCurrCalendar().time
            val laterDate = AppDateTimeUtil.compareDates(currDate, date)
            when (laterDate) {
                1 -> StatusOfTaking.NOT_TAKEN
                2 -> StatusOfTaking.WAITING
                else -> {
                    val currTime = AppDateTimeUtil.getCurrTime()
                    val laterTime = AppDateTimeUtil.compareTimes(currTime, time)
                    when (laterTime) {
                        1 -> StatusOfTaking.NOT_TAKEN
                        else -> StatusOfTaking.WAITING
                    }
                }
            }
        }
    }

    data class ScheduledMedicineForDay(
        val medicineName: String,
        val doseSize: String,
        val time: String,
        val statusOfTaking: String,
        val statusOfTakingColorId: Int
    )

    data class ScheduledMedicineForList(
        val medicineName: String,
        val durationType: String,
        val durationDates: String,
        val daysType: String,
        val timeOfTaking: String
    )

    enum class StatusOfTaking(val statusString: String, val statusColorId: Int) {
        WAITING("oczekujący", R.color.colorDarkerGray),
        TAKEN("przyjęty", R.color.colorStateGood),
        NOT_TAKEN("nieprzyjęty", R.color.colorStateSmall)
    }
}