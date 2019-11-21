package com.example.medihelper.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.data.local.model.MedicinePlanEntity
import com.example.medihelper.data.local.pojo.MedicinePlanEntityAndTimeDoseListAndMedicineEntityAndPlannedMedicineList
import com.example.medihelper.data.local.pojo.MedicinePlanEntityAndTimeDoseEntityList
import com.example.medihelper.data.local.pojo.MedicinePlanEntityAndTimeDoseListAndMedicineEntity

@Dao
interface MedicinePlanDao {

    @Insert
    suspend fun insert(entity: MedicinePlanEntity): Long

    @Update
    suspend fun update(entity: MedicinePlanEntity)

    @Query("DELETE FROM medicines_plans WHERE medicine_plan_id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM medicines_plans WHERE medicine_plan_id = :id")
    suspend fun getById(id: Int): MedicinePlanEntity

    @Query("SELECT * FROM medicines_plans WHERE medicine_plan_id = :id")
    suspend fun getWithTimeDoseListById(id: Int): MedicinePlanEntityAndTimeDoseEntityList

    @Query("SELECT medicine_plan_remote_id FROM medicines_plans WHERE medicine_plan_id = :id")
    suspend fun getRemoteIdById(id: Int): Long?

    @Query("SELECT * FROM medicines_plans mp JOIN medicines m ON mp.medicine_id = m.medicine_id WHERE mp.medicine_plan_id = :id")
    fun getWithTimeDoseListAndMedicineAndPlannedMedicineListLiveById(id: Int): LiveData<MedicinePlanEntityAndTimeDoseListAndMedicineEntityAndPlannedMedicineList>

    @Query("SELECT * FROM medicines_plans mp JOIN medicines m ON mp.medicine_id = m.medicine_id WHERE mp.person_id = :id")
    fun getWithTimeDoseListAndMedicineListLiveByPersonId(id: Int): LiveData<List<MedicinePlanEntityAndTimeDoseListAndMedicineEntity>>

    //remote depended operations
    @Insert
    suspend fun insert(entityList: List<MedicinePlanEntity>)

    @Update
    suspend fun update(entityList: List<MedicinePlanEntity>)

    @Query("DELETE FROM medicines_plans")
    suspend fun deleteAll()

    @Query("DELETE FROM medicines_plans WHERE medicine_plan_remote_id NOT IN (:remoteIdList)")
    suspend fun deleteByRemoteIdNotIn(remoteIdList: List<Long>)

    @Query("SELECT * FROM medicines_plans")
    suspend fun getAllList(): List<MedicinePlanEntity>

    @Query("SELECT medicine_plan_id FROM medicines_plans WHERE medicine_plan_remote_id = :remoteId")
    suspend fun getIdByRemoteId(remoteId: Long): Int?

    @Query("SELECT * FROM medicines_plans WHERE medicine_plan_synchronized = 0")
    suspend fun getEntityListToSync(): List<MedicinePlanEntity>
}