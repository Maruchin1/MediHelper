package com.example.medihelper.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medihelper.localdatabase.entities.MedicinePlan
import com.example.medihelper.localdatabase.entities.PlannedMedicine
import io.reactivex.Completable

@Dao
interface MedicinePlanDAO  {

    @Insert
    fun insert(medicinePlan: MedicinePlan): Long

    @Query("SELECT * FROM medicines_plans")
    fun getAllLive(): LiveData<List<MedicinePlan>>

    @Query("SELECT * FROM medicines_plans WHERE medicine_plan_id = :medicinePlanID")
    fun getById(medicinePlanID: Int): MedicinePlan

    @Query("SELECT * FROM medicines_plans WHERE medicine_plan_id = :medicinePlanID")
    fun getByIdLive(medicinePlanID: Int): LiveData<MedicinePlan>

    @Delete
    fun delete(medicinePlan: MedicinePlan)

    @Update
    fun update(medicinePlan: MedicinePlan)
}