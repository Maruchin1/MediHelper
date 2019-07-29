package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.FieldMutableLiveData
import com.example.medihelper.Repository
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.ScheduledMedicine

class AddToScheduleViewModel : ViewModel() {
    private val TAG = AddToScheduleViewModel::class.simpleName

    val medicinesListLive = Repository.getMedicinesLive()
    val medicinesTypesListLive = Repository.getMedicineTypesLive()

    val selectedMedicineLive = MutableLiveData<Medicine>()
    val selectedMedicineNameLive: LiveData<String>
    val selectedMedicineStateLive: LiveData<String>

    val scheduleTypeLive = MutableLiveData<ScheduledMedicine.ScheduleType>()
    val startDateStringLive = MutableLiveData<String>()
    val endDateStringLive = MutableLiveData<String>()

    val scheduleDaysLive = MutableLiveData<ScheduledMedicine.ScheduleDays>()
    val daysOfWeekLive = FieldMutableLiveData<ScheduledMedicine.DaysOfWeek>()
    val intervalOfDaysLive = MutableLiveData<Int>()

    init {
        selectedMedicineNameLive = Transformations.map(selectedMedicineLive) { medicine ->
            medicine.name
        }
        selectedMedicineStateLive = Transformations.map(selectedMedicineLive) { medicine ->
            val state = "${medicine.currState}/${medicine.packageSize}"
            val type = medicine.medicineTypeID?.let { medicineTypeID ->
                findMedicineTypeName(medicineTypeID)
            }
            "Aktualny stan: $state $type"
        }
        daysOfWeekLive.value = ScheduledMedicine.DaysOfWeek()
        intervalOfDaysLive.value = 0
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

    fun findMedicineTypeName(medicineTypeID: Int): String {
        return medicinesTypesListLive.value?.find { medicineType ->
            medicineType.medicineTypeID == medicineTypeID
        }?.typeName ?: "brak typu"
    }
}