package com.example.medihelper.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.pojos.PlannedMedicineDetails
import com.example.medihelper.localdatabase.pojos.PlannedMedicineItem
import java.util.*

@Dao
interface PlannedMedicineDAO {

    @Insert
    fun insert(plannedMedicineEntity: PlannedMedicineEntity)

    @Insert
    fun insert(plannedMedicineEntityList: List<PlannedMedicineEntity>)

    @Delete
    fun delete(plannedMedicineEntity: PlannedMedicineEntity)

    @Update
    fun update(plannedMedicineEntity: PlannedMedicineEntity)

    @Query("SELECT * FROM planned_medicines")
    fun getAll(): List<PlannedMedicineEntity>

    @Query("SELECT * FROM planned_medicines WHERE planned_medicine_id = :plannedMedicineID")
    fun getByID(plannedMedicineID: Int): PlannedMedicineEntity

    @Query("SELECT * FROM planned_medicines pm JOIN medicines_plans mp ON pm.medicine_plan_id = mp.medicine_plan_id JOIN medicines m ON mp.medicine_id = m.medicine_id JOIN medicine_types mt ON m.medicine_type_id = mt.medicine_type_id WHERE pm.planned_medicine_id = :plannedMedicineID")
    fun getPlannedMedicineDetailsLive(plannedMedicineID: Int): LiveData<PlannedMedicineDetails>

    @Query("SELECT * FROM planned_medicines pm JOIN medicines_plans mp ON pm.medicine_plan_id = mp.medicine_plan_id JOIN medicines m ON mp.medicine_id = m.medicine_id JOIN medicine_types mt ON m.medicine_type_id = mt.medicine_type_id WHERE pm.planned_date = :date")
    fun getPlannedMedicineByDateListLive(date: Date): LiveData<List<PlannedMedicineItem>>
}