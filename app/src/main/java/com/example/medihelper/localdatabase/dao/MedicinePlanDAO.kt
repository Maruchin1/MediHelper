package com.example.medihelper.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.pojos.MedicinePlanHistory
import com.example.medihelper.localdatabase.pojos.MedicinePlanItem

@Dao
interface MedicinePlanDAO  {

    @Insert
    suspend fun insert(medicinePlanEntity: MedicinePlanEntity): Long

    @Query("DELETE FROM medicines_plans WHERE medicine_plan_id = :medicinePlanID")
    suspend fun delete(medicinePlanID: Int)

    @Update
    suspend fun update(medicinePlanEntity: MedicinePlanEntity)

    @Query("SELECT * FROM medicines_plans WHERE medicine_plan_id = :medicinePlanID")
    suspend fun getEntity(medicinePlanID: Int): MedicinePlanEntity

    @Query("SELECT * FROM medicines_plans mp JOIN medicines m ON mp.medicine_id = m.medicine_id WHERE person_id = :personID")
    fun getMedicinePlanItemListLive(personID: Int): LiveData<List<MedicinePlanItem>>

    @Query("SELECT * FROM medicines_plans mp JOIN medicines m ON mp.medicine_id = m.medicine_id WHERE mp.medicine_plan_id = :medicinePlanID")
    fun getMedicinePlanHistoryLive(medicinePlanID: Int): LiveData<MedicinePlanHistory>
}
