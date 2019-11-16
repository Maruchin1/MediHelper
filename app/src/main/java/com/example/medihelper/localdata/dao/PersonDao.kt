package com.example.medihelper.localdata.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.localdata.entity.PersonEntity
import com.example.medihelper.localdata.pojo.PersonEditData
import com.example.medihelper.localdata.pojo.PersonItem
import com.example.medihelper.localdata.pojo.PersonOptionsData

@Dao
interface PersonDao {

    @Insert
    suspend fun insert(entity: PersonEntity): Long

    @Insert
    suspend fun insert(entityList: List<PersonEntity>)

    @Update
    suspend fun update(entity: PersonEntity)

    @Update
    suspend fun update(entityList: List<PersonEntity>)

    @Query("DELETE FROM persons WHERE person_id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM persons")
    suspend fun deleteAllWithMain()

    @Query("SELECT * FROM persons WHERE person_id = :id")
    suspend fun getEntity(id: Int): PersonEntity

    @Query("SELECT * FROM persons")
    suspend fun getEntityList(): List<PersonEntity>

    @Query("SELECT * FROM persons WHERE person_id = :id")
    suspend fun getItem(id: Int): PersonItem

    @Query("SELECT * FROM persons WHERE person_id = :id")
    suspend fun getEditData(id: Int): PersonEditData

    @Query("SELECT person_id FROM persons WHERE main_person = 1")
    suspend fun getMainPersonId(): Int?

    @Query("SELECT * FROM persons WHERE person_id = :id")
    fun getItemLive(id: Int): LiveData<PersonItem>

    @Query("SELECT person_color_res_id FROM persons WHERE person_id = :id")
    fun getColorLive(id: Int): LiveData<Int>

    @Query("SELECT * FROM persons WHERE person_id = :id")
    fun getOptionsDataLive(id: Int): LiveData<PersonOptionsData>

    @Query("SELECT * FROM persons")
    fun getItemListLive(): LiveData<List<PersonItem>>

    @Query("SELECT * FROM persons p JOIN medicines_plans mp ON p.person_id = mp.person_id JOIN medicines m ON mp.medicine_id = m.medicine_id GROUP BY p.person_id HAVING m.medicine_id = :medicineId")
    fun getItemListLiveByMedicineID(medicineId: Int): LiveData<List<PersonItem>>

    @Query("SELECT person_id FROM persons WHERE main_person = 1")
    fun getMainIdLive(): LiveData<Int>

    @Query("SELECT * FROM persons WHERE main_person = 1")
    fun getMainItemLive(): LiveData<PersonItem>

    @Query("SELECT person_color_res_id FROM persons WHERE main_person = 1")
    fun getMainColorLive(): LiveData<Int>

    // ServerSync
    @Query("SELECT person_id FROM persons WHERE person_remote_id = :remoteId")
    suspend fun getIdByRemoteId(remoteId: Long): Int?

    @Query("SELECT person_remote_id FROM persons WHERE person_id = :id")
    suspend fun getRemoteIdById(id: Int): Long?

    @Query("SELECT * FROM persons WHERE synchronized_with_server = 0 AND main_person = 0")
    suspend fun getEntityListToSync(): List<PersonEntity>

    @Query("DELETE FROM persons WHERE person_remote_id NOT IN (:remoteIdList) AND main_person = 0")
    suspend fun deleteByRemoteIdNotIn(remoteIdList: List<Long>)

    @Query("DELETE FROM persons WHERE main_person = 0")
    suspend fun deleteAllWithoutMain()
}