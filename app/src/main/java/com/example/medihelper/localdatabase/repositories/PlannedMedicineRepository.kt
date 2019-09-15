package com.example.medihelper.localdatabase.repositories

import androidx.lifecycle.LiveData
import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.pojos.PlannedMedicineDetails
import com.example.medihelper.localdatabase.pojos.PlannedMedicineItem



interface PlannedMedicineRepository {

    suspend fun insert(plannedMedicineEntity: PlannedMedicineEntity)

    suspend fun insert(plannedMedicineEntityList: List<PlannedMedicineEntity>)

    suspend fun update(plannedMedicineEntity: PlannedMedicineEntity)

    suspend fun update(plannedMedicineEntityList: List<PlannedMedicineEntity>)

    suspend fun delete(plannedMedicineID: Int)

    suspend fun getEntity(plannedMedicineID: Int): PlannedMedicineEntity

    suspend fun getEntityList(): List<PlannedMedicineEntity>

    fun getDetailsLive(plannedMedicineID: Int): LiveData<PlannedMedicineDetails>

    fun getItemListLiveByDate(date: AppDate, personID: Int): LiveData<List<PlannedMedicineItem>>
}