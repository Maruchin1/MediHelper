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
    fun getEntityList(): List<PlannedMedicineEntity>

    @Query("SELECT * FROM planned_medicines WHERE planned_medicine_id = :plannedMedicineID")
    fun getEntity(plannedMedicineID: Int): PlannedMedicineEntity

    @Query("SELECT * FROM planned_medicines pm JOIN medicines_plans mp ON pm.medicine_plan_id = mp.medicine_plan_id JOIN medicines m ON mp.medicine_id = m.medicine_id WHERE pm.planned_medicine_id = :plannedMedicineID")
    fun getDetailsLive(plannedMedicineID: Int): LiveData<PlannedMedicineDetails>

    @Query("SELECT * FROM planned_medicines pm JOIN medicines_plans mp ON pm.medicine_plan_id = mp.medicine_plan_id JOIN medicines m ON mp.medicine_id = m.medicine_id JOIN persons p ON mp.person_id = p.person_id WHERE pm.planned_date = :date AND p.person_id = :personID")
    fun getItemByDateListLive(date: Date, personID: Int): LiveData<List<PlannedMedicineItem>>
}