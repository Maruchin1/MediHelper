package com.example.medihelper.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medihelper.localdatabase.entities.MedicineEntity
import com.example.medihelper.localdatabase.pojos.MedicineDetails
import com.example.medihelper.localdatabase.pojos.MedicineEditData
import com.example.medihelper.localdatabase.pojos.MedicineItem

@Dao
interface MedicineDAO {

    @Insert
    suspend fun insert(medicineEntity: MedicineEntity)

    @Update
    suspend fun update(medicineEntity: MedicineEntity)

    @Query("DELETE FROM medicines WHERE medicine_id = :medicineID")
    suspend fun delete(medicineID: Int)

    @Query("SELECT * FROM medicines WHERE medicine_id = :medicineID")
    suspend fun getEntity(medicineID: Int): MedicineEntity

    @Query("SELECT * FROM medicines")
    fun getItemListLive(): LiveData<List<MedicineItem>>

    @Query("SELECT * FROM medicines WHERE LOWER(medicine_name) LIKE '%' || LOWER(:searchQuery) || '%'")
    fun getFilteredItemListLive(searchQuery: String): LiveData<List<MedicineItem>>

    @Query("SELECT * FROM medicines WHERE medicine_id = :medicineID")
    fun getItemLive(medicineID: Int): LiveData<MedicineItem>

    @Query("SELECT * FROM medicines WHERE medicine_id = :medicineID")
    fun getEditDataLive(medicineID: Int): LiveData<MedicineEditData>

    @Query("SELECT * FROM medicines WHERE medicine_id = :medicineID")
    fun getMedicineDetailsLive(medicineID: Int): LiveData<MedicineDetails>
}