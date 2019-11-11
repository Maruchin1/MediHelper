package com.example.medihelper.localdata.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medihelper.localdata.type.AppDate
import com.example.medihelper.localdata.entity.PlannedMedicineEntity
import com.example.medihelper.localdata.pojo.PlannedMedicineAlarmData
import com.example.medihelper.localdata.pojo.PlannedMedicineDetails
import com.example.medihelper.localdata.pojo.PlannedMedicineItem

@Dao
interface PlannedMedicineDao {

    @Insert
    suspend fun insert(entity: PlannedMedicineEntity): Long

    @Insert
    suspend fun insert(entityList: List<PlannedMedicineEntity>)

    @Update
    suspend fun update(entity: PlannedMedicineEntity)

    @Update
    suspend fun update(entityList: List<PlannedMedicineEntity>)

    @Query("DELETE FROM planned_medicines WHERE planned_medicine_id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM planned_medicines WHERE planned_medicine_id IN (:idList)")
    suspend fun delete(idList: List<Int>)

    @Query("DELETE FROM planned_medicines WHERE planned_medicine_remote_id NOT IN (:remoteIdList)")
    suspend fun deleteByRemoteIdNotIn(remoteIdList: List<Long>)

    @Query("DELETE FROM planned_medicines WHERE medicine_plan_id = :medicinePlanId AND planned_date >= :date")
    suspend fun deleteFromDateByMedicinePlanID(date: AppDate, medicinePlanId: Int)

    @Query("SELECT * FROM planned_medicines WHERE planned_medicine_id = :id")
    suspend fun getEntityById(id: Int): PlannedMedicineEntity

    @Query("SELECT * FROM planned_medicines")
    suspend fun getEntityList(): List<PlannedMedicineEntity>

    @Query("SELECT * FROM planned_medicines WHERE synchronized_with_server = 0")
    suspend fun getEntityListToSync(): List<PlannedMedicineEntity>

    @Query("SELECT planned_medicine_id FROM planned_medicines WHERE planned_medicine_remote_id = :remoteId")
    suspend fun getIdByRemoteId(remoteId: Long): Int?

    @Query("SELECT planned_medicine_remote_id FROM  planned_medicines WHERE planned_medicine_id = :id")
    suspend fun getRemoteIdById(id: Int): Long?

    @Query(
        value = "SELECT * FROM planned_medicines pm " +
                "JOIN medicines_plans mp ON pm.medicine_plan_id = mp.medicine_plan_id " +
                "JOIN persons p ON mp.person_id = p.person_id " +
                "JOIN medicines m ON mp.medicine_id = m.medicine_id " +
                "WHERE pm.planned_medicine_id = :id"
    )
    suspend fun getAlarmData(id: Int): PlannedMedicineAlarmData

    @Query(
        value = "SELECT planned_medicine_remote_id FROM planned_medicines " +
                "WHERE planned_medicine_id = :id"
    )
    suspend fun getRemoteId(id: Int): Long?

    @Query(
        value = "SELECT * FROM planned_medicines pm " +
                "JOIN medicines_plans mp ON pm.medicine_plan_id = mp.medicine_plan_id " +
                "JOIN medicines m ON mp.medicine_id = m.medicine_id " +
                "WHERE pm.planned_medicine_id = :id"
    )
    fun getDetailsLive(id: Int): LiveData<PlannedMedicineDetails>

    @Query(
        value = "SELECT * FROM planned_medicines pm " +
                "JOIN medicines_plans mp ON pm.medicine_plan_id = mp.medicine_plan_id " +
                "JOIN medicines m ON mp.medicine_id = m.medicine_id " +
                "JOIN persons p ON mp.person_id = p.person_id " +
                "WHERE pm.planned_date = :date " +
                "AND p.person_id = :personId"
    )
    fun getItemListLiveByDate(date: AppDate, personId: Int): LiveData<List<PlannedMedicineItem>>





}