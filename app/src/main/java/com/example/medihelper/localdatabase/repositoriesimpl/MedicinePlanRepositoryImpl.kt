package com.example.medihelper.localdatabase.repositoriesimpl

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.localdatabase.entities.MedicinePlanEntity
import com.example.medihelper.localdatabase.pojos.MedicinePlanHistory
import com.example.medihelper.localdatabase.pojos.MedicinePlanItem
import com.example.medihelper.localdatabase.repositories.MedicinePlanRepository

class MedicinePlanRepositoryImpl(private val medicinePlanDao: MedicinePlanDao) : MedicinePlanRepository {

    override suspend fun insert(medicinePlanEntity: MedicinePlanEntity) = medicinePlanDao.insert(medicinePlanEntity).toInt()

    override suspend fun insert(medicinePlanEntityList: List<MedicinePlanEntity>) = medicinePlanDao.insert(medicinePlanEntityList)

    override suspend fun update(medicinePlanEntity: MedicinePlanEntity) = medicinePlanDao.update(medicinePlanEntity)

    override suspend fun delete(medicinePlanID: Int) = medicinePlanDao.delete(medicinePlanID)

    override suspend fun deleteAll() = medicinePlanDao.deleteAll()

    override suspend fun getEntity(medicinePlanID: Int) = medicinePlanDao.getEntity(medicinePlanID)

    override suspend fun getEntityList() = medicinePlanDao.getEntityList()

    override fun getItemListLive(personID: Int) = medicinePlanDao.getItemListLive(personID)

    override fun getHistoryLive(medicinePlanID: Int) = medicinePlanDao.getHistoryLive(medicinePlanID)
}

@Dao
interface MedicinePlanDao {

    @Insert
    suspend fun insert(medicinePlanEntity: MedicinePlanEntity): Long

    @Insert
    suspend fun insert(medicinePlanEntityList: List<MedicinePlanEntity>)

    @Update
    suspend fun update(medicinePlanEntity: MedicinePlanEntity)

    @Query("DELETE FROM medicines_plans WHERE medicine_plan_id = :medicinePlanID")
    suspend fun delete(medicinePlanID: Int)

    @Query("DELETE FROM medicines_plans")
    suspend fun deleteAll()

    @Query("SELECT * FROM medicines_plans WHERE medicine_plan_id = :medicinePlanID")
    suspend fun getEntity(medicinePlanID: Int): MedicinePlanEntity

    @Query("SELECT * FROM medicines_plans")
    suspend fun getEntityList(): List<MedicinePlanEntity>

    @Query("SELECT * FROM medicines_plans mp JOIN medicines m ON mp.medicine_id = m.medicine_id WHERE person_id = :personID")
    fun getItemListLive(personID: Int): LiveData<List<MedicinePlanItem>>

    @Query("SELECT * FROM medicines_plans mp JOIN medicines m ON mp.medicine_id = m.medicine_id WHERE mp.medicine_plan_id = :medicinePlanID")
    fun getHistoryLive(medicinePlanID: Int): LiveData<MedicinePlanHistory>
}