package com.example.medihelper.localdatabase.repositoriesimpl

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.localdatabase.entities.DeletedEntity
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.example.medihelper.localdatabase.pojos.PersonOptionsData
import com.example.medihelper.localdatabase.repositories.PersonRepository

class PersonRepositoryImpl(
    private val personDao: PersonDao,
    private val deletedEntityDao: DeletedEntityDao
) : PersonRepository {

    override suspend fun insert(personEntity: PersonEntity) = personDao.insert(personEntity).toInt()

    override suspend fun update(personEntity: PersonEntity) =
        personDao.update(personEntity.apply { synchronizedWithServer = false })

    override suspend fun delete(personID: Int) {
        personDao.getRemoteID(personID)?.let { remoteID ->
            deletedEntityDao.insert(DeletedEntity(DeletedEntity.EntityType.PERSON, remoteID))
        }
        personDao.delete(personID)
    }

    override suspend fun deleteAllWithMain()  = personDao.deleteAllWithMain()

    override suspend fun getEntity(personID: Int) = personDao.getEntity(personID)

    override suspend fun getEntityList() = personDao.getEntityList()

    override suspend fun getItem(personID: Int) = personDao.getItem(personID)

    override suspend fun getMainPersonID() = personDao.getMainPersonID()

    override suspend fun getMainPersonEntity() = personDao.getMainPersonEntity()

    override fun getItemLive(personID: Int) = personDao.getItemLive(personID)

    override fun getOptionsDataLive(personID: Int) = personDao.getOptionsDataLive(personID)

    override fun getItemListLive() = personDao.getItemListLive()

    override fun getItemListLiveByMedicineID(medicineID: Int) = personDao.getItemListLiveByMedicineID(medicineID)

    override fun getMainPersonIDLive() = personDao.getMainPersonIdLive()

    override fun getMainPersonItemLive() = personDao.getMainPersonItemLive()

    override fun getMainPersonColorLive() = personDao.getMainPersonColorLive()

    // ServerSyncRepository

    override suspend fun insertSynchronized(entityList: List<PersonEntity>) = personDao.insert(entityList)

    override suspend fun updateSynchronized(entityList: List<PersonEntity>) = personDao.update(entityList)

    override suspend fun resetSynchronizationData() = personDao.resetSynchronizationData()

    override suspend fun deleteByRemoteIDNotIn(remoteIDList: List<Long>) = personDao.deleteByRemoteIDNotIn(remoteIDList)

    override suspend fun deleteAll() = personDao.deleteAll()

    override suspend fun clearDeletedRemoteIDList() = deletedEntityDao.deleteDeletedRemoteIDList(DeletedEntity.EntityType.PERSON)

    override suspend fun getEntityListToSync() = personDao.getEntityListToSync()

    override suspend fun getDeletedRemoteIDList() = deletedEntityDao.getDeletedRemoteIDList(DeletedEntity.EntityType.PERSON)

    override suspend fun getRemoteID(localID: Int) = personDao.getRemoteID(localID)

    override suspend fun getLocalIDByRemoteID(remoteID: Long) = personDao.getIDByRemoteID(remoteID)
}

@Dao
interface PersonDao {

    @Insert
    suspend fun insert(personEntity: PersonEntity): Long

    @Insert
    suspend fun insert(personEntityList: List<PersonEntity>)

    @Update
    suspend fun update(personEntity: PersonEntity)

    @Update
    suspend fun update(personEntityList: List<PersonEntity>)

    @Query("UPDATE persons SET synchronized_with_server = 0, person_remote_id = null")
    suspend fun resetSynchronizationData()

    @Query("DELETE FROM persons WHERE person_id = :personID")
    suspend fun delete(personID: Int)

    @Query("DELETE FROM persons WHERE person_remote_id NOT IN (:personRemoteIDList) AND main_person = 0")
    suspend fun deleteByRemoteIDNotIn(personRemoteIDList: List<Long>)

    @Query("DELETE FROM persons WHERE main_person = 0")
    suspend fun deleteAll()

    @Query("DELETE FROM persons")
    suspend fun deleteAllWithMain()

    @Query("SELECT * FROM persons WHERE person_id = :personID")
    suspend fun getEntity(personID: Int): PersonEntity

    @Query("SELECT * FROM persons")
    suspend fun getEntityList(): List<PersonEntity>

    @Query("SELECT * FROM persons WHERE synchronized_with_server = 0 AND main_person = 0")
    suspend fun getEntityListToSync(): List<PersonEntity>

    @Query("SELECT * FROM persons WHERE person_id = :personID")
    suspend fun getItem(personID: Int): PersonItem

    @Query("SELECT person_id FROM persons WHERE main_person = 1")
    suspend fun getMainPersonID(): Int?

    @Query("SELECT * FROM persons WHERE main_person = 1")
    suspend fun getMainPersonEntity(): PersonEntity

    @Query("SELECT person_remote_id FROM persons WHERE person_id = :personID")
    suspend fun getRemoteID(personID: Int): Long?

    @Query("SELECT person_id FROM persons WHERE person_remote_id = :personRemoteID")
    suspend fun getIDByRemoteID(personRemoteID: Long): Int?

    @Query("SELECT * FROM persons WHERE person_id = :personID")
    fun getItemLive(personID: Int): LiveData<PersonItem>

    @Query("SELECT * FROM persons WHERE person_id = :personID")
    fun getOptionsDataLive(personID: Int): LiveData<PersonOptionsData>

    @Query("SELECT * FROM persons")
    fun getItemListLive(): LiveData<List<PersonItem>>

    @Query("SELECT * FROM persons p JOIN medicines_plans mp ON p.person_id = mp.person_id JOIN medicines m ON mp.medicine_id = m.medicine_id GROUP BY p.person_id HAVING m.medicine_id = :medicineID")
    fun getItemListLiveByMedicineID(medicineID: Int): LiveData<List<PersonItem>>

    @Query("SELECT person_id FROM persons WHERE main_person = 1")
    fun getMainPersonIdLive(): LiveData<Int>
    
    @Query("SELECT * FROM persons WHERE main_person = 1")
    fun getMainPersonItemLive(): LiveData<PersonItem>

    @Query("SELECT person_color_res_id FROM persons WHERE main_person = 1")
    fun getMainPersonColorLive(): LiveData<Int>
}