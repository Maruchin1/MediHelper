package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import java.sql.Time
import java.util.*

class ScheduleViewModel : ViewModel() {

    val medicineListLive = AppRepository.getMedicinesLive()
    val medicineTypeListLive = AppRepository.getMedicineTypesLive()
    val scheduledMedicineListLive = AppRepository.getScheduledMedicinesLive()

    fun getScheduledMedicinesByDate(date: Date): LiveData<List<ScheduledMedicine>> {
        return Transformations.map(scheduledMedicineListLive) { scheduledMedicineList ->
            scheduledMedicineList.filter { scheduledMedicine ->
                scheduledMedicine.isScheduledForDate(date)
            }
        }
    }

    fun getScheduledMedicinesForDay(scheduledMedicineList: List<ScheduledMedicine>): List<ScheduledMedicineForDay> {
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
}