package com.example.medihelper.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medihelper.localdatabase.entities.MedicineEntity
import com.example.medihelper.localdatabase.pojos.MedicineDetails
import com.example.medihelper.localdatabase.pojos.MedicineEditData
import com.example.medihelper.localdatabase.pojos.MedicineKitItem

@Dao
interface MedicineDAO {

    @Insert
    fun insert(medicineEntity: MedicineEntity)

    @Update
    fun update(medicineEntity: MedicineEntity)

    @Query("DELETE FROM medicines WHERE medicine_id = :medicineID")
    fun delete(medicineID: Int)

    @Query("SELECT * FROM medicines m JOIN medicine_types mt ON m.medicine_type_id = mt.medicine_type_id")
    fun getKitItemListLive(): LiveData<List<MedicineKitItem>>

    @Query("SELECT * FROM medicines m JOIN medicine_types mt ON m.medicine_type_id = mt.medicine_type_id WHERE m.medicine_id = :medicineID")
    fun getKitItemLive(medicineID: Int): LiveData<MedicineKitItem>

    @Query("SELECT * FROM medicines m JOIN medicine_types mt ON m.medicine_type_id = mt.medicine_type_id WHERE m.medicine_id = :medicineID")
    fun getEditDataLive(medicineID: Int): LiveData<MedicineEditData>

    @Query("SELECT * FROM medicines m JOIN medicine_types mt ON m.medicine_type_id = mt.medicine_type_id WHERE m.medicine_id = :medicineID")
    fun getMedicineDetailsLive(medicineID: Int): LiveData<MedicineDetails>
}