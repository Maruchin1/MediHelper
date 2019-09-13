package com.example.medihelper.localdatabase.repositories

import androidx.lifecycle.LiveData
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.pojos.PlannedMedicineDetails
import com.example.medihelper.localdatabase.pojos.PlannedMedicineItem
import java.util.*

interface PlannedMedicineRepository {

    suspend fun insert(plannedMedicineEntity: PlannedMedicineEntity)

    suspend fun update(plannedMedicineEntity: PlannedMedicineEntity)

    suspend fun delete(plannedMedicineID: Int)

    suspend fun getEntity(plannedMedicineID: Int): PlannedMedicineEntity

    fun getDetailsLive(plannedMedicineID: Int): LiveData<PlannedMedicineDetails>

    fun getItemListLiveByDate(date: Date, personID: Int): LiveData<List<PlannedMedicineItem>>
}