package com.example.medihelper.localdatabase.repositories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.pojos.PersonItem

interface PersonRepository {

    suspend fun insert(personEntity: PersonEntity): Long

    suspend fun update(personEntity: PersonEntity)

    suspend fun delete(personID: Int)

    suspend fun getItem(personID: Int): PersonItem

    fun getItemListLive(): LiveData<List<PersonItem>>

    fun getItemListLiveByMedicineID(medicineID: Int): LiveData<List<PersonItem>>
}