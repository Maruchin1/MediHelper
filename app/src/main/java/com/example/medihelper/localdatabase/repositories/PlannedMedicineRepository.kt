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

    suspend fun delete(plannedMedicineIDList: List<Int>)

    suspend fun deleteByRemoteIDNotIn(remoteIDList: List<Long>)

    suspend fun deleteAll()

    suspend fun getEntity(plannedMedicineID: Int): PlannedMedicineEntity

    suspend fun getEntityList(): List<PlannedMedicineEntity>

    suspend fun getEntityListToSync(): List<PlannedMedicineEntity>

    suspend fun getRemoteID(plannedMedicineID: Int): Long?

    suspend fun getIDByRemoteID(plannedMedicineRemoteID: Long): Int?

    suspend fun getIDListFromDateByMedicinePlanID(date: AppDate, medicinePlanID: Int): List<Int>

    suspend fun getDeletedRemoteIDList(): List<Long>

    suspend fun clearDeletedRemoteIDList()

    fun getDetailsLive(plannedMedicineID: Int): LiveData<PlannedMedicineDetails>

    fun getItemListLiveByDate(date: AppDate, personID: Int): LiveData<List<PlannedMedicineItem>>
}