package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.DateUtil
import com.example.medihelper.Repository
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType
import java.text.SimpleDateFormat
import java.util.*

class ScheduleViewModel : ViewModel() {

    val medicinesListLive = Repository.getMedicinesLive()
    val medicinesTypesListLive = Repository.getMedicineTypesLive()

    val currDateLive = MutableLiveData<Date>()
    val currDayMonthLive: LiveData<String>
    val currWeekDayLive: LiveData<String>

    init {
        currDayMonthLive = Transformations.map(currDateLive) {
            DateUtil.dayAndMonthName(it)
        }
        currWeekDayLive = Transformations.map(currDateLive) {
            DateUtil.weekDayName(it)
        }
    }

    fun getScheduledMedicinesByDateLive(date: Date) =
        Repository.getScheduledMedicinesByDateLive(date)

    fun findMedicineById(medicineID: Int): Medicine? {
        return medicinesListLive.value?.find { medicine ->
            medicine.medicineID == medicineID
        }
    }

    fun findMedicineTypeById(medicineTypeID: Int): MedicineType? {
        return medicinesTypesListLive.value?.find { medicineType ->
            medicineType.medicineTypeID == medicineTypeID
        }
    }
}