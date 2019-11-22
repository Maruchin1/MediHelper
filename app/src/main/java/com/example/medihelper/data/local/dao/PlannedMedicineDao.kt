package com.example.medihelper.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.data.local.model.MedicinePlanEntity
import com.example.medihelper.data.local.model.PlannedMedicineEntity
import com.example.medihelper.data.local.pojo.PlannedMedicineEntityAndMedicineEntity
import com.example.medihelper.domain.entities.AppDate

@Dao
interface PlannedMedicineDao {

    @Insert
    suspend fun insert(entityList: List<PlannedMedicineEntity>)

    @Update
    suspend fun update(entity: PlannedMedicineEntity)

    @Update
    suspend fun update(entityList: List<PlannedMedicineEntity>)

    @Query("DELETE FROM planned_medicines WHERE medicine_plan_id = :medicinePlanId AND planned_date >= :date")
    suspend fun deleteFromDateByMedicinePlanId(date: AppDate, medicinePlanId: Int)

    @Query("SELECT * FROM planned_medicines WHERE planned_medicine_id = :id")
    suspend fun getById(id: Int): PlannedMedicineEntity

    @Query("SELECT planned_medicine_remote_id FROM planned_medicines WHERE planned_medicine_id = :id")
    suspend fun getRemoteIdById(id: Int): Long?

    @Query("SELECT planned_medicine_remote_id FROM planned_medicines WHERE medicine_plan_id = :id AND planned_date >= :date")
    suspend fun getRemoteIdListFromDateByMedicinePlanId(date: AppDate, id: Int): List<Long?>

    @Query("SELECT * FROM planned_medicines")
    suspend fun getAllList(): List<PlannedMedicineEntity>

    @Query("SELECT * FROM planned_medicines pm JOIN medicines_plans mp ON pm.medicine_plan_id = mp.medicine_plan_id JOIN medicines m ON mp.medicine_id = m.medicine_id WHERE pm.planned_medicine_id = :id")
    fun getWithMedicineLiveById(id: Int): LiveData<PlannedMedicineEntityAndMedicineEntity>

    @Query("SELECT * FROM planned_medicines pm JOIN medicines_plans mp ON pm.medicine_plan_id = mp.medicine_plan_id JOIN medicines m ON mp.medicine_id = m.medicine_id JOIN persons p ON mp.person_id = p.person_id WHERE pm.planned_date = :date AND p.person_id = :personId")
    fun getWithMedicineListLiveByDateAndPerson(date: AppDate, personId: Int): LiveData<List<PlannedMedicineEntityAndMedicineEntity>>

    //remote depended operations
    @Query("DELETE FROM medicines_plans")
    suspend fun deleteAll()

    @Query("DELETE FROM planned_medicines WHERE planned_medicine_remote_id NOT IN (:remoteIdList)")
    suspend fun deleteByRemoteIdNotIn(remoteIdList: List<Long>)

    @Query("SELECT planned_medicine_id FROM planned_medicines WHERE planned_medicine_remote_id = :remoteId")
    suspend fun getIdByRemoteId(remoteId: Long): Int?

    @Query("SELECT * FROM planned_medicines WHERE planned_medicine_synchronized = 0")
    suspend fun getEntityListToSync(): List<PlannedMedicineEntity>
}