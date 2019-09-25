package com.example.medihelper.localdatabase.repositoriesimpl

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.localdatabase.entities.DeletedEntity
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.example.medihelper.localdatabase.repositories.PersonRepository

class PersonRepositoryImpl(
    private val personDao: PersonDao,
    private val deletedEntityDao: DeletedEntityDao
) : PersonRepository {

    override suspend fun insert(personEntity: PersonEntity) = personDao.insert(personEntity).toInt()

    override suspend fun insert(personEntityList: List<PersonEntity>) = personDao.insert(personEntityList)

    override suspend fun update(personEntity: PersonEntity) =
        personDao.update(personEntity.apply { synchronizedWithServer = false })

    override suspend fun delete(personID: Int) {
        personDao.getRemoteID(personID)?.let { remoteID ->
            deletedEntityDao.insert(DeletedEntity(DeletedEntity.EntityType.PERSON, remoteID))
        }
        personDao.delete(personID)
    }

    override suspend fun deleteAll() = personDao.deleteAll()

    override suspend fun getEntity(personID: Int) = personDao.getEntity(personID)

    override suspend fun getEntityList() = personDao.getEntityList()

    override suspend fun getEntityListToSync() = personDao.getEntityListToSync()

    override suspend fun getItem(personID: Int) = personDao.getItem(personID)

    override suspend fun getMainPersonID() = personDao.getMainPersonID()

    override suspend fun getRemoteID(personID: Int) = personDao.getRemoteID(personID)

    override suspend fun getIDByRemoteID(personRemoteID: Long) = personDao.getIDByRemoteID(personRemoteID)

    override suspend fun getDeletedRemoteIDList() = deletedEntityDao.getDeletedRemoteIDListByEntityType(DeletedEntity.EntityType.PERSON)

    override suspend fun clearDeletedRemoteIDList() =
        deletedEntityDao.deleteDeleterRemoteIDListByEntityType(DeletedEntity.EntityType.PERSON)

    override fun getItemLive(personID: Int) = personDao.getItemLive(personID)

    override fun getItemListLive() = personDao.getItemListLive()

    override fun getItemListLiveByMedicineID(medicineID: Int) = personDao.getItemListLiveByMedicineID(medicineID)

    override fun getMainPersonIDLive() = personDao.getMainPersonIdLive()
}

@Dao
interface PersonDao {

    @Insert
    suspend fun insert(personEntity: PersonEntity): Long

    @Insert
    suspend fun insert(personEntityList: List<PersonEntity>)

    @Update
    suspend fun update(personEntity: PersonEntity)

    @Query("DELETE FROM persons WHERE person_id = :personID")
    suspend fun delete(personID: Int)

    @Query("DELETE FROM persons")
    suspend fun deleteAll()

    @Query("SELECT * FROM persons WHERE person_id = :personID")
    suspend fun getEntity(personID: Int): PersonEntity

    @Query("SELECT * FROM persons")
    suspend fun getEntityList(): List<PersonEntity>

    @Query("SELECT * FROM persons WHERE synchronized_with_server = 0")
    suspend fun getEntityListToSync(): List<PersonEntity>

    @Query("SELECT * FROM persons WHERE person_id = :personID")
    suspend fun getItem(personID: Int): PersonItem

    @Query("SELECT person_id FROM persons WHERE main_person = 1")
    suspend fun getMainPersonID(): Int?

    @Query("SELECT person_remote_id FROM persons WHERE person_id = :personID")
    suspend fun getRemoteID(personID: Int): Long?

    @Query("SELECT person_id FROM persons WHERE person_remote_id = :personRemoteID")
    suspend fun getIDByRemoteID(personRemoteID: Long): Int

    @Query("SELECT * FROM persons WHERE person_id = :personID")
    fun getItemLive(personID: Int): LiveData<PersonItem>

    @Query("SELECT * FROM persons")
    fun getItemListLive(): LiveData<List<PersonItem>>

    @Query("SELECT * FROM persons p JOIN medicines_plans mp ON p.person_id = mp.person_id JOIN medicines m ON mp.medicine_id = m.medicine_id GROUP BY p.person_id HAVING m.medicine_id = :medicineID")
    fun getItemListLiveByMedicineID(medicineID: Int): LiveData<List<PersonItem>>

    @Query("SELECT person_id FROM persons WHERE main_person = 1")
    fun getMainPersonIdLive(): LiveData<Int>
}