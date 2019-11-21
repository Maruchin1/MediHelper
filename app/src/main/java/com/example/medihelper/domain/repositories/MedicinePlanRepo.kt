package com.example.medihelper.domain.repositories

import androidx.lifecycle.LiveData
import com.example.medihelper.domain.entities.MedicinePlan
import com.example.medihelper.domain.entities.MedicinePlanWithMedicine
import com.example.medihelper.domain.entities.MedicinePlanWithMedicineAndPlannedMedicines

interface MedicinePlanRepo {
    suspend fun insert(medicinePlan: MedicinePlan): Int
    suspend fun update(medicinePlan: MedicinePlan)
    suspend fun deleteById(id: Int)
    suspend fun getById(id: Int): MedicinePlan
    fun getWithMedicineAndPlannedMedicinesLiveById(id: Int): LiveData<MedicinePlanWithMedicineAndPlannedMedicines>
    fun getWithMedicineListLiveByPersonId(id: Int): LiveData<List<MedicinePlanWithMedicine>>
}