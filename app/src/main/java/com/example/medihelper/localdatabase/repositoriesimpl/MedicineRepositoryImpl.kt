package com.example.medihelper.localdatabase.repositoriesimpl

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.localdatabase.entities.DeletedEntity
import com.example.medihelper.localdatabase.entities.MedicineEntity
import com.example.medihelper.localdatabase.pojos.MedicineDetails
import com.example.medihelper.localdatabase.pojos.MedicineItem
import com.example.medihelper.localdatabase.repositories.MedicineRepository

class MedicineRepositoryImpl(
    private val medicineDao: MedicineDao,
    private val deletedEntityDao: DeletedEntityDao
) : MedicineRepository {

    override suspend fun insert(medicineEntity: MedicineEntity) = medicineDao.insert(medicineEntity)

    override suspend fun update(medicineEntity: MedicineEntity) =
        medicineDao.update(medicineEntity.apply { synchronizedWithServer = false })

    override suspend fun delete(medicineID: Int) {
        medicineDao.getRemoteID(medicineID)?.let { remoteID ->
            deletedEntityDao.insert(DeletedEntity(DeletedEntity.EntityType.MEDICINE, remoteID))
        }
        medicineDao.delete(medicineID)
    }

    override suspend fun getEntity(medicineID: Int) = medicineDao.getEntity(medicineID)

    override suspend fun getEntityList() = medicineDao.getEntityList()

    override suspend fun getDetails(medicineID: Int) = medicineDao.getDetails(medicineID)

    override fun getItemListLive(): LiveData<List<MedicineItem>> = medicineDao.getItemListLive()

    override fun getFilteredItemListLive(searchQuery: String) = medicineDao.getFilteredItemListLive(searchQuery)

    override fun getItemLive(medicineID: Int) = medicineDao.getItemLive(medicineID)

    override fun getDetailsLive(medicineID: Int) = medicineDao.getDetailsLive(medicineID)

    // ServerSyncRepository

    override suspend fun insertSynchronized(entityList: List<MedicineEntity>) = medicineDao.insert(entityList)

    override suspend fun updateSynchronized(entityList: List<MedicineEntity>) = medicineDao.update(entityList)

    override suspend fun deleteByRemoteIDNotIn(remoteIDList: List<Long>) = medicineDao.deleteByRemoteIDNotIn(remoteIDList)

    override suspend fun deleteAll() = medicineDao.deleteAll()

    override suspend fun clearDeletedRemoteIDList() = deletedEntityDao.deleteDeletedRemoteIDList(DeletedEntity.EntityType.MEDICINE)

    override suspend fun getEntityListToSync() = medicineDao.getEntityListToSync()

    override suspend fun getDeletedRemoteIDList() = deletedEntityDao.getDeletedRemoteIDList(DeletedEntity.EntityType.MEDICINE)

    override suspend fun getRemoteID(localID: Int) = medicineDao.getRemoteID(localID)

    override suspend fun getLocalIDByRemoteID(remoteID: Long) = medicineDao.getIDByRemoteID(remoteID)
}

@Dao
interface MedicineDao {

    @Insert
    suspend fun insert(medicineEntity: MedicineEntity)

    @Insert
    suspend fun insert(medicineEntityList: List<MedicineEntity>)

    @Update
    suspend fun update(medicineEntity: MedicineEntity)

    @Update
    suspend fun update(medicineEntityList: List<MedicineEntity>)

    @Query("DELETE FROM medicines WHERE medicine_id = :medicineID")
    suspend fun delete(medicineID: Int)

    @Query("DELETE FROM medicines WHERE medicine_remote_id NOT IN (:medicineRemoteIDList)")
    suspend fun deleteByRemoteIDNotIn(medicineRemoteIDList: List<Long>)

    @Query("DELETE FROM medicines")
    suspend fun deleteAll()

    @Query("SELECT * FROM medicines WHERE medicine_id = :medicineID")
    suspend fun getEntity(medicineID: Int): MedicineEntity

    @Query("SELECT * FROM medicines")
    suspend fun getEntityList(): List<MedicineEntity>

    @Query("SELECT * FROM medicines WHERE synchronized_with_server = 0")
    suspend fun getEntityListToSync(): List<MedicineEntity>

    @Query("SELECT * FROM medicines WHERE medicine_id = :medicineID")
    suspend fun getDetails(medicineID: Int): MedicineDetails

    @Query("SELECT medicine_remote_id FROM medicines WHERE medicine_id = :medicineID")
    suspend fun getRemoteID(medicineID: Int): Long?

    @Query("SELECT medicine_id FROM medicines WHERE medicine_remote_id = :medicineRemoteID")
    suspend fun getIDByRemoteID(medicineRemoteID: Long): Int?

    @Query("SELECT * FROM medicines")
    fun getItemListLive(): LiveData<List<MedicineItem>>

    @Query("SELECT * FROM medicines WHERE LOWER(medicine_name) LIKE '%' || LOWER(:searchQuery) || '%'")
    fun getFilteredItemListLive(searchQuery: String): LiveData<List<MedicineItem>>

    @Query("SELECT * FROM medicines WHERE medicine_id = :medicineID")
    fun getItemLive(medicineID: Int): LiveData<MedicineItem>

    @Query("SELECT * FROM medicines WHERE medicine_id = :medicineID")
    fun getDetailsLive(medicineID: Int): LiveData<MedicineDetails>
}
