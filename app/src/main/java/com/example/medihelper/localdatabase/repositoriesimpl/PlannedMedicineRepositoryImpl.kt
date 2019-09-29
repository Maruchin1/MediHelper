package com.example.medihelper.localdatabase.repositoriesimpl

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medihelper.AppDate
import com.example.medihelper.localdatabase.entities.DeletedEntity
import com.example.medihelper.localdatabase.entities.PlannedMedicineEntity
import com.example.medihelper.localdatabase.pojos.PlannedMedicineDetails
import com.example.medihelper.localdatabase.pojos.PlannedMedicineItem
import com.example.medihelper.localdatabase.repositories.PlannedMedicineRepository


class PlannedMedicineRepositoryImpl(
    private val plannedMedicineDao: PlannedMedicineDao,
    private val deletedEntityDao: DeletedEntityDao
) : PlannedMedicineRepository {

    override suspend fun insert(plannedMedicineEntity: PlannedMedicineEntity) =
        plannedMedicineDao.insert(plannedMedicineEntity)

    override suspend fun insert(plannedMedicineEntityList: List<PlannedMedicineEntity>) =
        plannedMedicineDao.insert(plannedMedicineEntityList)

    override suspend fun update(plannedMedicineEntity: PlannedMedicineEntity) =
        plannedMedicineDao.update(plannedMedicineEntity.apply { synchronizedWithServer = false })

    override suspend fun update(plannedMedicineEntityList: List<PlannedMedicineEntity>) =
        plannedMedicineDao.update(plannedMedicineEntityList.map {
            it.apply {
                synchronizedWithServer = false
            }
        })

    override suspend fun delete(plannedMedicineIDList: List<Int>) {
        plannedMedicineIDList.forEach { plannedMedicineID ->
            plannedMedicineDao.getRemoteID(plannedMedicineID)?.let { remoteID ->
                deletedEntityDao.insert(
                    DeletedEntity(
                        DeletedEntity.EntityType.PLANNED_MEDICINE,
                        remoteID
                    )
                )
            }
        }
        plannedMedicineDao.delete(plannedMedicineIDList)
    }

    override suspend fun getEntity(plannedMedicineID: Int) =
        plannedMedicineDao.getEntity(plannedMedicineID)

    override suspend fun getEntityList() = plannedMedicineDao.getEntityList()

    override suspend fun getIDListFromDateByMedicinePlanID(date: AppDate, medicinePlanID: Int) =
        plannedMedicineDao.getIDListFromDataByMedicinePlanID(date, medicinePlanID)

    override fun getDetailsLive(plannedMedicineID: Int) =
        plannedMedicineDao.getDetailsLive(plannedMedicineID)

    override fun getItemListLiveByDate(date: AppDate, personID: Int) =
        plannedMedicineDao.getItemListLiveByDate(date, personID)

    // ServerSyncRepository

    override suspend fun insertSynchronized(entityList: List<PlannedMedicineEntity>) =
        plannedMedicineDao.insert(entityList)

    override suspend fun updateSynchronized(entityList: List<PlannedMedicineEntity>) =
        plannedMedicineDao.update(entityList)

    override suspend fun resetSynchronizationData() = plannedMedicineDao.resetSynchronizationData()

    override suspend fun deleteByRemoteIDNotIn(remoteIDList: List<Long>) =
        plannedMedicineDao.deleteByRemoteIDNotIn(remoteIDList)

    override suspend fun deleteAll() = plannedMedicineDao.deleteAll()

    override suspend fun clearDeletedRemoteIDList() =
        deletedEntityDao.deleteDeletedRemoteIDList(DeletedEntity.EntityType.PLANNED_MEDICINE)

    override suspend fun getEntityListToSync() = plannedMedicineDao.getEntityListToSync()

    override suspend fun getDeletedRemoteIDList() =
        deletedEntityDao.getDeletedRemoteIDList(DeletedEntity.EntityType.PLANNED_MEDICINE)

    override suspend fun getRemoteID(localID: Int) = plannedMedicineDao.getRemoteID(localID)

    override suspend fun getLocalIDByRemoteID(remoteID: Long) =
        plannedMedicineDao.getIDByRemoteID(remoteID)
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

    @Query("UPDATE planned_medicines SET synchronized_with_server = 0, planned_medicine_remote_id = null")
    suspend fun resetSynchronizationData()

    @Query("DELETE FROM planned_medicines WHERE planned_medicine_id IN (:plannedMedicineIDList)")
    suspend fun delete(plannedMedicineIDList: List<Int>)

    @Query("DELETE FROM planned_medicines WHERE planned_medicine_remote_id NOT IN (:remoteIDList)")
    suspend fun deleteByRemoteIDNotIn(remoteIDList: List<Long>)

    @Query("DELETE FROM planned_medicines")
    suspend fun deleteAll()

    @Query("SELECT * FROM planned_medicines WHERE planned_medicine_id = :plannedMedicineID")
    suspend fun getEntity(plannedMedicineID: Int): PlannedMedicineEntity

    @Query("SELECT * FROM planned_medicines")
    suspend fun getEntityList(): List<PlannedMedicineEntity>

    @Query("SELECT * FROM planned_medicines WHERE synchronized_with_server = 0")
    suspend fun getEntityListToSync(): List<PlannedMedicineEntity>

    @Query("SELECT planned_medicine_remote_id FROM planned_medicines WHERE planned_medicine_id = :plannedMedicineID")
    suspend fun getRemoteID(plannedMedicineID: Int): Long?

    @Query("SELECT planned_medicine_id FROM planned_medicines WHERE planned_medicine_remote_id = :plannedMedicineRemoteID")
    suspend fun getIDByRemoteID(plannedMedicineRemoteID: Long): Int?

    @Query("SELECT planned_medicine_id FROM planned_medicines WHERE medicine_plan_id = :medicinePlanID AND planned_date >= :date")
    suspend fun getIDListFromDataByMedicinePlanID(date: AppDate, medicinePlanID: Int): List<Int>

    @Query("SELECT * FROM planned_medicines pm JOIN medicines_plans mp ON pm.medicine_plan_id = mp.medicine_plan_id JOIN medicines m ON mp.medicine_id = m.medicine_id WHERE pm.planned_medicine_id = :plannedMedicineID")
    fun getDetailsLive(plannedMedicineID: Int): LiveData<PlannedMedicineDetails>

    @Query("SELECT * FROM planned_medicines pm JOIN medicines_plans mp ON pm.medicine_plan_id = mp.medicine_plan_id JOIN medicines m ON mp.medicine_id = m.medicine_id JOIN persons p ON mp.person_id = p.person_id WHERE pm.planned_date = :date AND p.person_id = :personID")
    fun getItemListLiveByDate(date: AppDate, personID: Int): LiveData<List<PlannedMedicineItem>>
}