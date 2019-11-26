package com.maruchin.medihelper.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.maruchin.medihelper.data.local.model.TimeDoseEntity

@Dao
interface TimeDoseDao {

    @Insert
    suspend fun insert(entityList: List<TimeDoseEntity>)

    @Query("DELETE FROM times_doses WHERE medicine_plan_id = :id")
    suspend fun deleteByMedicinePlanId(id: Int)

    @Query("SELECT * FROM times_doses WHERE medicine_plan_id = :id")
    suspend fun getEntityListByMedicinePlanId(id: Int): List<TimeDoseEntity>
}