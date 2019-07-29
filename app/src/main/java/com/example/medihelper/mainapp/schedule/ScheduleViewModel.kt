package com.example.medihelper.mainapp.schedule

import androidx.lifecycle.ViewModel
import com.example.medihelper.Repository
import com.example.medihelper.localdatabase.entities.Medicine
import com.example.medihelper.localdatabase.entities.MedicineType

class ScheduleViewModel : ViewModel() {

    val medicinesListLive = Repository.getMedicinesLive()
    val medicinesTypesListLive = Repository.getMedicineTypesLive()

//    fun getScheduledMedicinesByDateLive(date: Date): LiveData<List<ScheduledMedicine>> {
//        return Transformations.map(Repository.getScheduledMedicinesByDateLive(date)) {
//            it.sortedBy { scheduledMedicine ->
//                scheduledMedicine.date
//            }
//        }
//    }

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