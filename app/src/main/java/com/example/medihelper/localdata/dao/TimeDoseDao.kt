package com.example.medihelper.localdata.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.medihelper.localdata.entity.TimeDoseEntity

@Dao
interface TimeDoseDao {

    @Insert
    suspend fun insert(entityList: List<TimeDoseEntity>)

    @Query("DELETE FROM times_doses WHERE medicine_plan_id = :medicinePlanId")
    suspend fun deleteAllByMedicinePlanId(medicinePlanId: Int)

    @Query("SELECT * FROM times_doses WHERE medicine_plan_id = :medicinePlanId")
    suspend fun getEntityListByMedicinePlanId(medicinePlanId: Int): List<TimeDoseEntity>
}