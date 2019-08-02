package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppDateTimeUtil
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.Medicine
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

    fun getScheduledMedicinesByDate(date: Date): LiveData<List<ScheduledMedicine>> {
        return Transformations.map(scheduledMedicineListLive) { scheduledMedicineList ->
            scheduledMedicineList.filter { scheduledMedicine ->
                scheduledMedicine.isScheduledForDate(date)
            }
        }
    }

    fun getScheduledMedicineForDayList(scheduledMedicineList: List<ScheduledMedicine>): List<ScheduledMedicineForDay> {
        val scheduledMedicineForDayList = ArrayList<ScheduledMedicineForDay>()
        scheduledMedicineList.forEach { scheduledMedicine ->
            val medicine = getMedicineById(scheduledMedicine.medicineID)
            if (medicine != null) {
                val medicineType = getMedicineTypeById(medicine.medicineTypeID!!)
                scheduledMedicine.timeOfTakingList.forEach { timeOfTaking ->
                    scheduledMedicineForDayList.add(
                        ScheduledMedicineForDay(
                            medicineID = medicine.medicineID,
                            medicineName = medicine.name,
                            medicineTypeName = medicineType?.typeName ?: "brak typu",
                            doseSize = timeOfTaking.doseSize,
                            time = timeOfTaking.time
                        )
                    )
                }
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

    data class ScheduledMedicineForDay(
        val medicineID: Int,
        val medicineName: String,
        val medicineTypeName: String,
        var doseSize: Int,
        var time: Time
    )

    data class ScheduledMedicineForList(
        val medicineName: String,
        val durationType: String,
        val durationDates: String,
        val daysType: String,
        val timeOfTaking: String
    )
}