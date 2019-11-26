package com.maruchin.medihelper.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.maruchin.medihelper.data.local.model.MedicineEntity

@Dao
interface MedicineDao {

    @Insert
    suspend fun insert(medicineEntity: MedicineEntity)

    @Update
    suspend fun update(medicineEntity: MedicineEntity)

    @Query("DELETE FROM medicines WHERE medicine_id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM medicines WHERE medicine_id = :id")
    suspend fun getById(id: Int): MedicineEntity

    @Query("SELECT medicine_remote_id FROM medicines WHERE medicine_id = :id")
    suspend fun getRemoteIdById(id: Int): Long?

    @Query("SELECT * FROM medicines WHERE medicine_id = :id")
    fun getLiveById(id: Int): LiveData<MedicineEntity>

    @Query("SELECT * FROM medicines")
    fun getAllListLive(): LiveData<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE LOWER(medicine_name) LIKE '%' || LOWER(:nameQuery) || '%'")
    fun getListLiveFilteredByName(nameQuery: String): LiveData<List<MedicineEntity>>

    //remote depended operations
    @Insert
    suspend fun insert(entityList: List<MedicineEntity>)

    @Update
    suspend fun update(entityList: List<MedicineEntity>)

    @Query("DELETE FROM medicines")
    suspend fun deleteAll()

    @Query("DELETE FROM medicines WHERE medicine_remote_id NOT IN (:remoteIdList)")
    suspend fun deleteByRemoteIdNotIn(remoteIdList: List<Long>)

    @Query("SELECT * FROM medicines")
    suspend fun getAllList(): List<MedicineEntity>

    @Query("SELECT medicine_id FROM medicines WHERE medicine_remote_id = :remoteId")
    suspend fun getIdByRemoteId(remoteId: Long): Int?

    @Query("SELECT * FROM medicines WHERE medicine_synchronized = 0")
    suspend fun getEntityListToSync(): List<MedicineEntity>
}