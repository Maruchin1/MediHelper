package com.example.medihelper.mainapp.schedule

import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.medihelper.DateUtil
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

    val scheduleTypeLive = MutableLiveData<ScheduledMedicine.ScheduleType>()
    val startDateStringLive = MutableLiveData<String>()
    val endDateStringLive = MutableLiveData<String>()

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
    }
    fun findMedicineTypeName(medicineTypeID: Int): String {
        return medicinesTypesListLive.value?.find { medicineType ->
            medicineType.medicineTypeID == medicineTypeID
        }?.typeName ?: "brak typu"
    }
}