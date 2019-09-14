package com.example.medihelper.localdatabase.repositoriesimpl

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.pojos.PlannedMedicineDetails
import com.example.medihelper.localdatabase.pojos.PlannedMedicineItem
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository
import java.util.*

class PlannedMedicineRepositoryImpl(private val plannedMedicineDao: PlannedMedicineDao) : PlannedMedicineRepository {

    override suspend fun insert(plannedMedicineEntity: PlannedMedicineEntity) = plannedMedicineDao.insert(plannedMedicineEntity)

    override suspend fun update(plannedMedicineEntity: PlannedMedicineEntity) = plannedMedicineDao.update(plannedMedicineEntity)

    override suspend fun delete(plannedMedicineID: Int) = plannedMedicineDao.delete(plannedMedicineID)

    override suspend fun getEntity(plannedMedicineID: Int) = plannedMedicineDao.getEntity(plannedMedicineID)

    override fun getDetailsLive(plannedMedicineID: Int) = plannedMedicineDao.getDetailsLive(plannedMedicineID)

    override fun getItemListLiveByDate(date: Date, personID: Int) = plannedMedicineDao.getItemListLiveByDate(date, personID)
}

@Dao
interface PlannedMedicineDao {

    @Insert
    suspend fun insert(plannedMedicineEntity: PlannedMedicineEntity)

    @Insert
    suspend fun insert(plannedMedicineEntityList: List<PlannedMedicineEntity>)

    @Update
    suspend fun update(plannedMedicineEntity: PlannedMedicineEntity)

    @Update
    suspend fun update(plannedMedicineEntityList: List<PlannedMedicineEntity>)

    @Query("DELETE FROM planned_medicines WHERE planned_medicine_id = :plannedMedicineID")
    suspend fun delete(plannedMedicineID: Int)

    @Query("SELECT * FROM planned_medicines")
    suspend fun getEntityList(): List<PlannedMedicineEntity>

    @Query("SELECT * FROM planned_medicines WHERE planned_medicine_id = :plannedMedicineID")
    suspend fun getEntity(plannedMedicineID: Int): PlannedMedicineEntity

    @Query("SELECT * FROM planned_medicines pm JOIN medicines_plans mp ON pm.medicine_plan_id = mp.medicine_plan_id JOIN medicines m ON mp.medicine_id = m.medicine_id WHERE pm.planned_medicine_id = :plannedMedicineID")
    fun getDetailsLive(plannedMedicineID: Int): LiveData<PlannedMedicineDetails>

    @Query("SELECT * FROM planned_medicines pm JOIN medicines_plans mp ON pm.medicine_plan_id = mp.medicine_plan_id JOIN medicines m ON mp.medicine_id = m.medicine_id JOIN persons p ON mp.person_id = p.person_id WHERE pm.planned_date = :date AND p.person_id = :personID")
    fun getItemListLiveByDate(date: Date, personID: Int): LiveData<List<PlannedMedicineItem>>
}