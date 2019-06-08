package com.example.medihelper.mainapp.schedule

import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.R
import com.example.medihelper.Repository
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.ScheduledMedicine
import java.util.*
import kotlin.collections.ArrayList

class AddToScheduleViewModel : ViewModel() {
    private val TAG = AddToScheduleViewModel::class.simpleName

    val medicinesListLive = Repository.getMedicinesLive()
    val medicinesTypesListLive = Repository.getMedicineTypesLive()

    val selectedMedicineLive = MutableLiveData<Medicine>()
    val selectedMedicineNameLive: LiveData<String>
    val selectedMedicineStateLive: LiveData<String>
    val startDateLive = MutableLiveData<String>()
    val endDateLive = MutableLiveData<String>()
    val doseHoursListLive = MutableLiveData<List<DoseHour>>()

    init {
        selectedMedicineNameLive = Transformations.map(selectedMedicineLive) {
            it.name
        }
        selectedMedicineStateLive = Transformations.map(selectedMedicineLive) {
            val state = it.calcMedicineState()
            val type = findMedicineTypeName(it.medicineTypeID)
            "Aktualny stan: $state $type"
        }
        startDateLive.value = getCurrDateString()
        endDateLive.value = DATE_NOT_SELECTED_INFO
    }

    fun saveToSchedule(doseDaysId: Int, doseHoursId: Int) {
        selectedMedicineLive.value?.let { medicine ->
            startDateLive.value?.let { startDate ->
                val daysToSchedule = listOf(startDate)
                //todo obsłużyć pozostałe opcje wyboru dni
                doseHoursListLive.value?.let { hoursToSchedule ->
                    saveToSchedule(daysToSchedule, hoursToSchedule, medicine)
                }
            }
        }
    }

    private fun saveToSchedule(days: List<String>, doseHours: List<DoseHour>, medicine: Medicine) {
        val scheduledMedicinesList = ArrayList<ScheduledMedicine>()
        for (day in days) {
            for (doseHour in doseHours) {
                ScheduledMedicine(
                    medicineID = medicine.medicineID!!,
                    date = day,
                    time = doseHour.hour,
                    doseSize = doseHour.doseSize
                ).let {
                    scheduledMedicinesList.add(it)
                }
            }
        }
        Repository.insertAllScheduledMedicines(scheduledMedicinesList)
    }

    fun showSelectDateDialogFragment(context: Context, selectedDateMethod: (date: String) -> Unit) {
        val calendar = Calendar.getInstance()
        val currYear = calendar.get(Calendar.YEAR)
        val currMonth = calendar.get(Calendar.MONTH)
        val currDay = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(
            context,
            R.style.DateDialogPicker,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val correctMonth = month + 1
                val selectedDay = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val selectedMonth = if (correctMonth < 10) "0$correctMonth" else "$correctMonth"
                val selectedDate = "$selectedDay-$selectedMonth-$year"
                selectedDateMethod(selectedDate)
            },
            currYear,
            currMonth,
            currDay
        ).show()
    }

    fun changeDoseHoursListSeveralTimes(number: Int) {
        val defStartHour = 8
        val defEndHour = 20
        val defDoseCount = 1
        val doseHoursList = ArrayList<DoseHour>()
        if (number != 0) {
            val hoursDiff = defEndHour - defStartHour
            val hourInterval = hoursDiff / number
            for (i in 0 until number) {
                val hour = defStartHour + (hourInterval * i)
                val hourString = "$hour:00"
                doseHoursList.add(
                    DoseHour(
                        hour = hourString,
                        doseSize = defDoseCount
                    )
                )
            }
        }
        doseHoursListLive.value = doseHoursList
    }

    fun changeDoseHoursListHoursInterval(number: Int) {
        //todo metoda wyliczająca kolejne godziny dla interwału godzinowego
    }

    fun findMedicineTypeName(medicineTypeID: Int): String {
        return medicinesTypesListLive.value?.find { medicineType ->
            medicineType.medicineTypeID == medicineTypeID
        }?.typeName ?: "brak typu"
    }

    private fun getCurrDateString(): String {
        val calendar = Calendar.getInstance()
        val currYear = calendar.get(Calendar.YEAR)
        val currMonth = calendar.get(Calendar.MONTH) + 1
        val currDay = calendar.get(Calendar.DAY_OF_MONTH)

        val monthString = if (currMonth < 10) "0$currMonth" else "$currMonth"
        val dayString = if (currDay < 10) "0$currDay" else "$currDay"

        return "$dayString-$monthString-$currYear"
    }

    companion object {
        val DATE_NOT_SELECTED_INFO = "Nie określono"
    }
}