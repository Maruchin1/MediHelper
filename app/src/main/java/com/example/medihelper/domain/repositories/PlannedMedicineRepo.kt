package com.example.medihelper.domain.repositories

import androidx.lifecycle.LiveData
import com.example.medihelper.domain.entities.AppDate
import com.example.medihelper.domain.entities.PlannedMedicine
import com.example.medihelper.domain.entities.PlannedMedicineWithMedicine
import com.example.medihelper.domain.entities.PlannedMedicineWithMedicineAndPerson

interface PlannedMedicineRepo {
    suspend fun insert(plannedMedicineList: List<PlannedMedicine>)
    suspend fun update(plannedMedicine: PlannedMedicine)
    suspend fun update(plannedMedicineList: List<PlannedMedicine>)
    suspend fun deleteFromDateByMedicinePlanId(date: AppDate, medicinePlanId: Int)
    suspend fun getById(id: Int): PlannedMedicine
    suspend fun getAllList(): List<PlannedMedicine>
    suspend fun getWithMedicineAndPersonById(id: Int): PlannedMedicineWithMedicineAndPerson
    fun getWithMedicineLiveById(id: Int): LiveData<PlannedMedicineWithMedicine>
    fun getWithMedicineListLiveByDateAndPerson(
        date: AppDate,
        personId: Int
    ): LiveData<List<PlannedMedicineWithMedicine>>
}