package com.example.medihelper.localdata.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.data.local.model.MedicineEntity
import com.example.medihelper.localdata.pojo.MedicineDetails
import com.example.medihelper.localdata.pojo.MedicineEditData
import com.example.medihelper.localdata.pojo.MedicineItem

@Dao
interface MedicineDao {

    @Insert
    suspend fun insert(entity: MedicineEntity)

    @Insert
    suspend fun insert(entityList: List<MedicineEntity>)

    @Update
    suspend fun update(entity: MedicineEntity)

    @Update
    suspend fun update(entityList: List<MedicineEntity>)

    @Query("DELETE FROM medicines WHERE medicine_id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM medicines WHERE medicine_id = :id")
    suspend fun getEntity(id: Int): MedicineEntity

    @Query("SELECT * FROM medicines")
    suspend fun getEntityList(): List<MedicineEntity>

    @Query("SELECT * FROM medicines WHERE medicine_id = :id")
    suspend fun getDetails(id: Int): MedicineDetails

    @Query("SELECT * FROM medicines WHERE medicine_id = :id")
    suspend fun getEditData(id: Int): MedicineEditData

    @Query("SELECT * FROM medicines")
    fun getItemListLive(): LiveData<List<MedicineItem>>

    @Query("SELECT * FROM medicines WHERE LOWER(medicine_name) LIKE '%' || LOWER(:searchQuery) || '%'")
    fun getFilteredItemListLive(searchQuery: String): LiveData<List<MedicineItem>>

    @Query("SELECT * FROM medicines WHERE medicine_id = :id")
    fun getItemLive(id: Int): LiveData<MedicineItem>

    @Query("SELECT * FROM medicines WHERE medicine_id = :id")
    fun getDetailsLive(id: Int): LiveData<MedicineDetails>

    // Synchronization
    @Query("SELECT medicine_id FROM medicines WHERE medicine_remote_id = :remoteId")
    suspend fun getIdByRemoteId(remoteId: Long): Int?

    @Query("SELECT medicine_remote_id FROM medicines WHERE medicine_id = :id")
    suspend fun getRemoteIdById(id: Int): Long?

    @Query("SELECT * FROM medicines WHERE synchronized_with_server = 0")
    suspend fun getEntityListToSync(): List<MedicineEntity>

    @Query("DELETE FROM medicines WHERE medicine_remote_id NOT IN (:remoteIdList)")
    suspend fun deleteByRemoteIdNotIn(remoteIdList: List<Long>)

    @Query("DELETE FROM medicines")
    suspend fun deleteAll()
}