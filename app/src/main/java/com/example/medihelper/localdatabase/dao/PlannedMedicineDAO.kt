package com.example.medihelper.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medihelper.localdatabase.entities.PlannedMedicine
import java.util.*

@Dao
interface PlannedMedicineDAO {

    @Insert
    fun insert(plannedMedicine: PlannedMedicine)

    @Insert
    fun insert(plannedMedicineList: List<PlannedMedicine>)

    @Query("SELECT * FROM planned_medicines")
    fun getAllLive(): LiveData<List<PlannedMedicine>>

    @Query("SELECT * FROM planned_medicines")
    fun getAll(): List<PlannedMedicine>

    @Query("SELECT * FROM planned_medicines WHERE planned_date = :date ORDER BY planned_time")
    fun getByDateLive(date: Date): LiveData<List<PlannedMedicine>>

    @Query("SELECT * FROM planned_medicines WHERE planned_medicine_id = :plannedMedicineID")
    fun getByIdLive(plannedMedicineID: Int): LiveData<PlannedMedicine>

    @Delete
    fun delete(plannedMedicine: PlannedMedicine)

    @Update
    fun update(plannedMedicine: PlannedMedicine)
}