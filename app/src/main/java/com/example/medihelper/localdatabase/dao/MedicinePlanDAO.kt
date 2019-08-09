package com.example.medihelper.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.pojos.MedicinePlanItem

@Dao
interface MedicinePlanDAO  {

    @Insert
    fun insert(medicinePlanEntity: MedicinePlanEntity): Long

    @Query("DELETE FROM medicines_plans WHERE medicine_plan_id = :medicinePlanID")
    fun delete(medicinePlanID: Int)

    @Update
    fun update(medicinePlanEntity: MedicinePlanEntity)

    //    @Query("SELECT * FROM medicines_plans")
//    fun getAllLive(): LiveData<List<MedicinePlanEntity>>
//
//    @Query("SELECT * FROM medicines_plans WHERE medicine_plan_id = :medicinePlanID")
//    fun getById(medicinePlanID: Int): MedicinePlanEntity
//
//    @Query("SELECT * FROM medicines_plans WHERE medicine_plan_id = :medicinePlanID")
//    fun getByIdLive(medicinePlanID: Int): LiveData<MedicinePlanEntity>

    @Query("SELECT * FROM medicines_plans WHERE medicine_plan_id = :medicinePlanID")
    fun getByID(medicinePlanID: Int): MedicinePlanEntity

    @Query("SELECT * FROM medicines_plans mp JOIN medicines m ON mp.medicine_id = m.medicine_id JOIN medicine_types mt ON m.medicine_type_id = mt.medicine_type_id")
    fun getMedicinePlanListLive(): LiveData<List<MedicinePlanItem>>
}
