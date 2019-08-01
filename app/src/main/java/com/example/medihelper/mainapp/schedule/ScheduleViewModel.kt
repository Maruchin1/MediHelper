package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import java.util.*

class ScheduleViewModel : ViewModel() {

    val scheduledMedicineListLive = AppRepository.getScheduledMedicinesLive()

    fun getScheduledMedicinesByDate(date: Date): LiveData<List<ScheduledMedicine>> {
        return Transformations.map(scheduledMedicineListLive) { scheduledMedicineList ->
            scheduledMedicineList.filter { scheduledMedicine ->
                scheduledMedicine.isScheduledForDate(date)
            }
        }
    }

    fun getScheduledMedicinesForDay(scheduledMedicineList: List<ScheduledMedicine>): List<ScheduleDayFragment.ScheduledMedicineForDay> {
        val scheduledMedicineForDayList = ArrayList<ScheduleDayFragment.ScheduledMedicineForDay>()
        scheduledMedicineList.forEach { scheduledMedicine ->
            scheduledMedicine.timeOfTakingList.forEach { timeOfTaking ->
                scheduledMedicineForDayList.add(
                    ScheduleDayFragment.ScheduledMedicineForDay(
                        scheduledMedicine = scheduledMedicine,
                        doseSize = timeOfTaking.doseSize,
                        time = timeOfTaking.time
                    )
                )
            }
        }
        return scheduledMedicineForDayList
    }
}