package com.example.medihelper.localdatabase.repositories

import androidx.lifecycle.LiveData
import com.example.medihelper.localdatabase.entity.MedicinePlanEntity
import com.example.medihelper.localdatabase.pojos.MedicinePlanHistory
import com.example.medihelper.localdatabase.pojos.MedicinePlanItem

interface MedicinePlanRepository : ServerSyncRepository<MedicinePlanEntity> {

    suspend fun insert(medicinePlanEntity: MedicinePlanEntity): Int

    suspend fun update(medicinePlanEntity: MedicinePlanEntity)

    suspend fun delete(medicinePlanID: Int)

    suspend fun getEntity(medicinePlanID: Int): MedicinePlanEntity

    suspend fun getEntityList(): List<MedicinePlanEntity>

    fun getItemListLive(personID: Int): LiveData<List<MedicinePlanItem>>

    fun getHistoryLive(medicinePlanID: Int): LiveData<MedicinePlanHistory>
}