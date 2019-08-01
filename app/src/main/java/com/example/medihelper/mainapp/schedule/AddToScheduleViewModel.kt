package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.FieldMutableLiveData
import com.example.medihelper.AppRepository
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import java.util.*
import kotlin.collections.ArrayList

class AddToScheduleViewModel : ViewModel() {
    private val TAG = AddToScheduleViewModel::class.simpleName

    val medicinesListLive = AppRepository.getMedicinesLive()
    val medicinesTypesListLive = AppRepository.getMedicineTypesLive()

    val selectedMedicineLive = MutableLiveData<Medicine>()
    val selectedMedicineNameLive: LiveData<String>
    val selectedMedicineTypeLive: LiveData<MedicineType>
    val selectedMedicineStateLive: LiveData<String>

    val scheduleTypeLive = MutableLiveData<ScheduledMedicine.DurationType>()
    val startDateLive = MutableLiveData<Date>()
    val endDateLive = MutableLiveData<Date>()

    val scheduleDaysLive = MutableLiveData<ScheduledMedicine.DaysType>()
    val daysOfWeekLive = FieldMutableLiveData<ScheduledMedicine.DaysOfWeek>()
    val intervalOfDaysLive = MutableLiveData<Int>()

    val doseHourListLive = MutableLiveData<ArrayList<ScheduledMedicine.TimeOfTaking>>()

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
        doseHourListLive.value = arrayListOf(ScheduledMedicine.TimeOfTaking())
    }

    fun saveScheduledMedicine() {
        //todo zrobić to porządniej i z walidacją danych
        val scheduledMedicine = ScheduledMedicine(
            medicineID = selectedMedicineLive.value!!.medicineID,
            startDate = startDateLive.value!!,
            durationType = scheduleTypeLive.value!!,
            daysType = scheduleDaysLive.value!!,
            timeOfTakingList = doseHourListLive.value!!.toList()
        )
        if (scheduleTypeLive.value == ScheduledMedicine.DurationType.PERIOD) {
            scheduledMedicine.endDate = endDateLive.value
        }
        when (scheduleDaysLive.value) {
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

    private fun findMedicineType(medicineTypeID: Int): MedicineType? {
        return medicinesTypesListLive.value?.find { medicineType ->
            medicineType.medicineTypeID == medicineTypeID
        }
    }
}