package com.example.medihelper.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.data.local.model.MedicinePlanEntity
import com.example.medihelper.data.local.pojo.MedicinePlanEntityAndTimeDoseEntityList

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
}