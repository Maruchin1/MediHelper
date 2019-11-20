package com.example.medihelper.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.data.local.model.MedicineEntity

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
}