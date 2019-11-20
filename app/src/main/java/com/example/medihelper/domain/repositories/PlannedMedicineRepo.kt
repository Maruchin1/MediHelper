package com.example.medihelper.domain.repositories

import androidx.lifecycle.LiveData
import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.PlannedMedicine
import com.example.medihelper.domain.entities.PlannedMedicineWithMedicine

interface PlannedMedicineRepo {
    suspend fun update(plannedMedicine: PlannedMedicine)
    suspend fun update(plannedMedicineList: List<PlannedMedicine>)
    suspend fun getById(id: Int): PlannedMedicine
    suspend fun getAllList(): List<PlannedMedicine>
    fun getWithMedicineLiveById(id: Int): LiveData<PlannedMedicineWithMedicine>
    fun getWithMedicineListLiveByDateAndPerson(
        date: AppDate,
        personId: Int
    ): LiveData<List<PlannedMedicineWithMedicine>>
}