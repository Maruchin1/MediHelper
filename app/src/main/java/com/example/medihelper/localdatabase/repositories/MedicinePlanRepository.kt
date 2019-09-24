package com.example.medihelper.localdatabase.repositories

import androidx.lifecycle.LiveData
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.pojos.MedicinePlanHistory
import com.example.medihelper.localdatabase.pojos.MedicinePlanItem

interface MedicinePlanRepository {

    suspend fun insert(medicinePlanEntity: MedicinePlanEntity): Int

    suspend fun insert(medicinePlanEntityList: List<MedicinePlanEntity>)

    suspend fun update(medicinePlanEntity: MedicinePlanEntity)

    suspend fun delete(medicinePlanID: Int)

    suspend fun deleteAll()

    suspend fun getEntity(medicinePlanID: Int): MedicinePlanEntity

    suspend fun getEntityList(): List<MedicinePlanEntity>

    suspend fun getRemoteID(medicinePlanID: Int): Long

    suspend fun getIDByRemoteID(medicinePlanRemoteID: Long): Int

    fun getItemListLive(personID: Int): LiveData<List<MedicinePlanItem>>

    fun getHistoryLive(medicinePlanID: Int): LiveData<MedicinePlanHistory>
}