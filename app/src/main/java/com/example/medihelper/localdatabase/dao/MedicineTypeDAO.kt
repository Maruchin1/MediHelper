package com.example.medihelper.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.medihelper.localdatabase.entities.MedicineType

@Dao
interface MedicineTypeDAO {

    @Insert
    fun insertSingle(medicineType: MedicineType)

    @Query("SELECT * FROM medicine_types")
    fun getAllLive(): LiveData<List<MedicineType>>

    @Query("SELECT * FROM medicine_types")
    fun getAll(): List<MedicineType>

    @Query("SELECT * FROM medicine_types WHERE medicine_type_id = :mediciTypeId")
    fun getByIdLive(mediciTypeId: Int): LiveData<MedicineType>
}