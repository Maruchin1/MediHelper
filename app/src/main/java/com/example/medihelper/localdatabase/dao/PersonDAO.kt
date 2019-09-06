package com.example.medihelper.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.pojos.MedicineItem
import com.example.medihelper.localdatabase.pojos.PersonItem

@Dao
interface PersonDAO {

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

    @Query("SELECT * FROM persons")
    fun getItemListLive(): LiveData<List<PersonItem>>

    @Query("SELECT * FROM persons p JOIN medicines_plans mp ON p.person_id = mp.person_id JOIN medicines m ON mp.medicine_id = m.medicine_id WHERE m.medicine_id = :medicineID")
    fun getItemListLiveByMedicineID(medicineID: Int): LiveData<List<PersonItem>>

    @Query("SELECT * FROM persons WHERE person_id = :personID")
    fun getItemLive(personID: Int): LiveData<PersonItem>
}