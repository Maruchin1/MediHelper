package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.FieldMutableLiveData
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import java.util.*
import kotlin.collections.ArrayList

class AddScheduledMedicineViewModel : ViewModel() {
    private val TAG = AddScheduledMedicineViewModel::class.simpleName

    val medicinesListLive = AppRepository.getMedicinesLive()
    val medicinesTypesListLive = AppRepository.getMedicineTypesLive()

    val selectedMedicineLive = MutableLiveData<Medicine>()
    val selectedMedicineNameLive: LiveData<String>
    val selectedMedicineTypeLive: LiveData<MedicineType>
    val selectedMedicineStateLive: LiveData<String>

    val durationTypeLive = MutableLiveData<ScheduledMedicine.DurationType>()
    val startDateLive = MutableLiveData<Date>()
    val endDateLive = MutableLiveData<Date>()

    val daysTypeLive = MutableLiveData<ScheduledMedicine.DaysType>()
    val daysOfWeekLive = FieldMutableLiveData<ScheduledMedicine.DaysOfWeek>()
    val intervalOfDaysLive = MutableLiveData<Int>()

    val doseHourListLive = MutableLiveData<ArrayList<ScheduledMedicine.TimeOfTaking>>()

    private val selectedMedicineObserver = Observer<Medicine> { resetViewModel() }

    init {
        selectedMedicineNameLive = Transformations.map(selectedMedicineLive) { medicine ->
            medicine?.name
        }
        selectedMedicineTypeLive = Transformations.map(selectedMedicineLive) { medicine ->
            medicine?.medicineTypeID?.let { medicineTypeID ->
                findMedicineType(medicineTypeID)
            }
        }
        selectedMedicineStateLive = Transformations.map(selectedMedicineLive) { medicine ->
            "${medicine?.currState}/${medicine?.packageSize}"
        }
        selectedMedicineLive.observeForever(selectedMedicineObserver)
    }

    override fun onCleared() {
        super.onCleared()
        selectedMedicineLive.removeObserver(selectedMedicineObserver)
    }

    fun saveScheduledMedicine() {
        //todo zrobić to porządniej i z walidacją danych
        val scheduledMedicine = ScheduledMedicine(
            medicineID = selectedMedicineLive.value!!.medicineID,
            startDate = startDateLive.value!!,
            durationType = durationTypeLive.value!!,
            daysType = daysTypeLive.value!!,
            timeOfTakingList = doseHourListLive.value!!.toList()
        )
        if (durationTypeLive.value == ScheduledMedicine.DurationType.PERIOD) {
            scheduledMedicine.endDate = endDateLive.value
        }
        when (daysTypeLive.value) {
            ScheduledMedicine.DaysType.DAYS_OF_WEEK -> scheduledMedicine.daysOfWeek = daysOfWeekLive.value
            ScheduledMedicine.DaysType.INTERVAL_OF_DAYS -> scheduledMedicine.intervalOfDays = intervalOfDaysLive.value
        }
        AppRepository.insertScheduledMedicie(scheduledMedicine)
    }

    fun addDoseHour() {
        doseHourListLive.value?.let { doseHourList ->
            doseHourList.add(ScheduledMedicine.TimeOfTaking())
            doseHourListLive.value = doseHourListLive.value
        }
    }

    fun removeDoseHour(timeOfTaking: ScheduledMedicine.TimeOfTaking) {
        doseHourListLive.value?.let { doseHourList ->
            doseHourList.remove(timeOfTaking)
            doseHourListLive.value = doseHourListLive.value
        }
    }

    fun updateDoseHour(position: Int, timeOfTaking: ScheduledMedicine.TimeOfTaking) {
        doseHourListLive.value?.let { doseHourList ->
            doseHourList[position] = timeOfTaking
            doseHourListLive.value = doseHourListLive.value
        }
    }

    fun resetViewModel() {
        arrayOf(
            durationTypeLive,
            startDateLive,
            endDateLive,
            daysTypeLive
        ).forEach { field ->
            field.value = null
        }
        daysOfWeekLive.value = ScheduledMedicine.DaysOfWeek()
        intervalOfDaysLive.value = 0
        doseHourListLive.value = arrayListOf(ScheduledMedicine.TimeOfTaking())
    }

    private fun findMedicineType(medicineTypeID: Int): MedicineType? {
        return medicinesTypesListLive.value?.find { medicineType ->
            medicineType.medicineTypeID == medicineTypeID
        }
    }
}