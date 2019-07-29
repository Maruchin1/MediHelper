package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.FieldMutableLiveData
import com.example.medihelper.Repository
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import java.util.*
import kotlin.collections.ArrayList

class AddToScheduleViewModel : ViewModel() {
    private val TAG = AddToScheduleViewModel::class.simpleName

    val medicinesListLive = Repository.getMedicinesLive()
    val medicinesTypesListLive = Repository.getMedicineTypesLive()

    val selectedMedicineLive = MutableLiveData<Medicine>()
    val selectedMedicineNameLive: LiveData<String>
    val selectedMedicineTypeLive: LiveData<MedicineType>
    val selectedMedicineStateLive: LiveData<String>

    val scheduleTypeLive = MutableLiveData<ScheduledMedicine.ScheduleType>()
    val startDateLive = MutableLiveData<Date>()
    val endDateLive = MutableLiveData<Date>()

    val scheduleDaysLive = MutableLiveData<ScheduledMedicine.ScheduleDays>()
    val daysOfWeekLive = FieldMutableLiveData<ScheduledMedicine.DaysOfWeek>()
    val intervalOfDaysLive = MutableLiveData<Int>()

    val doseHourListLive = MutableLiveData<ArrayList<ScheduledMedicine.DoseHour>>()

    init {
        selectedMedicineNameLive = Transformations.map(selectedMedicineLive) { medicine ->
            medicine.name
        }
        selectedMedicineTypeLive = Transformations.map(selectedMedicineLive) { medicine ->
            medicine?.medicineTypeID?.let { medicineTypeID ->
                findMedicineType(medicineTypeID)
            }
        }
        selectedMedicineStateLive = Transformations.map(selectedMedicineLive) { medicine ->
            "${medicine.currState}/${medicine.packageSize}"
        }
        daysOfWeekLive.value = ScheduledMedicine.DaysOfWeek()
        intervalOfDaysLive.value = 0
        doseHourListLive.value = arrayListOf(ScheduledMedicine.DoseHour())
    }

    fun incrementIntervalOfDays() {
        intervalOfDaysLive.value = intervalOfDaysLive.value?.let { currInterval ->
            currInterval + 1
        }
    }

    fun decrementIntervalOfDays() {
        intervalOfDaysLive.value = intervalOfDaysLive.value?.let { currInterval ->
            var newInterval = currInterval - 1
            if (newInterval < 0) {
                newInterval = 0
            }
            newInterval
        }
    }

    private fun findMedicineType(medicineTypeID: Int): MedicineType? {
        return medicinesTypesListLive.value?.find { medicineType ->
            medicineType.medicineTypeID == medicineTypeID
        }
    }
}