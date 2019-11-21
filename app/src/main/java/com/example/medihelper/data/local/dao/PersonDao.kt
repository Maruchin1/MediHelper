package com.example.medihelper.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.data.local.model.PersonEntity

@Dao
interface PersonDao  {

    @Insert
    suspend fun insert(personEntity: PersonEntity)

    @Update
    suspend fun update(personEntity: PersonEntity)

    @Query("DELETE FROM persons WHERE person_id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM persons WHERE person_id = :id")
    suspend fun getById(id: Int): PersonEntity

    @Query("SELECT person_remote_id FROM persons WHERE person_id = :id")
    suspend fun getRemoteIdById(id: Int): Long?

    @Query("SELECT person_id FROM persons WHERE main_person = 1")
    suspend fun getMainId(): Int?

    @Query("SELECT * FROM persons WHERE person_id = :id")
    fun getLiveById(id: Int): LiveData<PersonEntity>

    @Query("SELECT * FROM persons WHERE main_person = 1")
    fun getMainLive(): LiveData<PersonEntity>

    @Query("SELECT person_id FROM persons WHERE main_person = 1")
    fun getMainIdLive(): LiveData<Int>

    @Query("SELECT person_color_res_id FROM persons WHERE main_person = 1")
    fun getMainColorIdLive(): LiveData<Int>

    @Query("SELECT * FROM persons")
    fun getAllListLive(): LiveData<List<PersonEntity>>

    @Query("SELECT * FROM persons p JOIN medicines_plans mp ON p.person_id = mp.person_id JOIN medicines m ON mp.medicine_id = m.medicine_id GROUP BY p.person_id HAVING m.medicine_id = :id")
    fun getListLiveByMedicineId(id: Int): LiveData<List<PersonEntity>>

    //remote depended operations
    @Insert
    suspend fun insert(entityList: List<PersonEntity>)

    @Update
    suspend fun update(entityList: List<PersonEntity>)

    @Query("DELETE FROM persons")
    suspend fun deleteAll()

    @Query("DELETE FROM persons WHERE person_remote_id NOT IN (:remoteIdList)")
    suspend fun deleteByRemoteIdNotIn(remoteIdList: List<Long>)

    @Query("SELECT * FROM persons")
    suspend fun getAllList(): List<PersonEntity>

    @Query("SELECT person_id FROM persons WHERE person_remote_id = :remoteId")
    suspend fun getIdByRemoteId(remoteId: Long): Int?

    @Query("SELECT * FROM persons WHERE person_synchronized = 0")
    suspend fun getEntityListToSync(): List<PersonEntity>
}