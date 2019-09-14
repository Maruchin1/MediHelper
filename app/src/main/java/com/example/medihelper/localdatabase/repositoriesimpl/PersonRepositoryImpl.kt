package com.example.medihelper.localdatabase.repositoriesimpl

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.example.medihelper.localdatabase.repositories.PersonRepository

class PersonRepositoryImpl(private val personDao: PersonDao) : PersonRepository {

    override suspend fun insert(personEntity: PersonEntity) = personDao.insert(personEntity).toInt()

    override suspend fun update(personEntity: PersonEntity) = personDao.update(personEntity)

    override suspend fun delete(personID: Int) = personDao.delete(personID)

    override suspend fun getItem(personID: Int) = personDao.getItem(personID)

    override fun getItemLive(personID: Int) = personDao.getItemLive(personID)

    override fun getItemListLive() = personDao.getItemListLive()

    override fun getItemListLiveByMedicineID(medicineID: Int) = personDao.getItemListLiveByMedicineID(medicineID)
}

@Dao
interface PersonDao {

    @Insert
    suspend fun insert(person: PersonEntity): Long

    @Update
    suspend fun update(person: PersonEntity)

    @Query("DELETE FROM persons WHERE person_id = :personID")
    suspend fun delete(personID: Int)

    @Query("SELECT * FROM persons WHERE person_id = :personID")
    suspend fun getItem(personID: Int): PersonItem

    @Query("SELECT COUNT(*) FROM persons")
    suspend fun getCount(): Int

    @Query("SELECT * FROM persons WHERE person_id = :personID")
    fun getItemLive(personID: Int): LiveData<PersonItem>

    @Query("SELECT * FROM persons")
    fun getItemListLive(): LiveData<List<PersonItem>>

    @Query("SELECT * FROM persons p JOIN medicines_plans mp ON p.person_id = mp.person_id JOIN medicines m ON mp.medicine_id = m.medicine_id GROUP BY p.person_id HAVING m.medicine_id = :medicineID")
    fun getItemListLiveByMedicineID(medicineID: Int): LiveData<List<PersonItem>>
}