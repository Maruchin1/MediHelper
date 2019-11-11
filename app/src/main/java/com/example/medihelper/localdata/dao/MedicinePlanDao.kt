package com.example.medihelper.localdata.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.localdata.entity.MedicinePlanEntity
import com.example.medihelper.localdata.pojo.MedicinePlanEditData
import com.example.medihelper.localdata.pojo.MedicinePlanHistory
import com.example.medihelper.localdata.pojo.MedicinePlanItem

@Dao
interface MedicinePlanDao {

    @Insert
    suspend fun insert(entity: MedicinePlanEntity): Long

    @Insert
    suspend fun insert(entityList: List<MedicinePlanEntity>)

    @Update
    suspend fun update(entity: MedicinePlanEntity)

    @Update
    suspend fun update(entityList: List<MedicinePlanEntity>)

    @Query("DELETE FROM medicines_plans WHERE medicine_plan_id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM medicines_plans WHERE medicine_plan_id = :id")
    suspend fun getEntity(id: Int): MedicinePlanEntity

    @Query("SELECT * FROM medicines_plans")
    suspend fun getEntityList(): List<MedicinePlanEntity>

    @Query("SELECT * FROM medicines_plans mp JOIN times_doses td ON mp.medicine_plan_id = td.medicine_plan_id WHERE mp.medicine_plan_id = :id")
    suspend fun getEditDataById(id: Int): MedicinePlanEditData

    @Query("SELECT * FROM medicines_plans mp JOIN medicines m ON mp.medicine_id = m.medicine_id WHERE person_id = :personId")
    fun getItemListLiveByPersonId(personId: Int): LiveData<List<MedicinePlanItem>>

    @Query("SELECT * FROM medicines_plans mp JOIN medicines m ON mp.medicine_id = m.medicine_id WHERE mp.medicine_plan_id = :id")
    fun getHistoryLive(id: Int): LiveData<MedicinePlanHistory>

    // ServerSync
    @Query("SELECT medicine_plan_id FROM medicines_plans WHERE medicine_plan_remote_id = :remoteId")
    suspend fun getIdByRemoteId(remoteId: Long): Int?

    @Query("SELECT medicine_plan_remote_id FROM medicines_plans WHERE medicine_plan_id = :id")
    suspend fun getRemoteIdById(id: Int): Long?

    @Query("SELECT * FROM medicines_plans WHERE synchronized_with_server = 0")
    suspend fun getEntityListToSync(): List<MedicinePlanEntity>

    @Query("DELETE FROM medicines_plans WHERE medicine_plan_remote_id NOT IN (:remoteIdList)")
    suspend fun deleteByRemoteIdNotIn(remoteIdList: List<Long>)
}