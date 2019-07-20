package com.example.medihelper.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medihelper.localdatabase.entities.Medicine

@Dao
interface MedicineDAO {

    @Insert
    fun insertSingle(medicine: Medicine)

    @Query("SELECT * FROM medicines ORDER BY name ASC")
    fun getAllLive(): LiveData<List<Medicine>>

    @Query("SELECT * FROM medicines ORDER BY name ASC")
    fun getAll(): List<Medicine>

    @Query("SELECT * FROM medicines WHERE medicine_id = :medicineId")
    fun getByIdLive(medicineId: Int): LiveData<Medicine>

    @Delete
    fun delete(medicine: Medicine)

    @Update
    fun update(medicine: Medicine)
}