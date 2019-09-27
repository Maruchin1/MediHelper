package com.example.medihelper.localdatabase.repositories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.pojos.PersonItem

interface PersonRepository : ServerSyncRepository<PersonEntity> {

    suspend fun insert(personEntity: PersonEntity): Int

    suspend fun update(personEntity: PersonEntity)

    suspend fun delete(personID: Int)

    suspend fun getEntity(personID: Int): PersonEntity

    suspend fun getEntityList(): List<PersonEntity>

    suspend fun getItem(personID: Int): PersonItem

    suspend fun getMainPersonID(): Int?

    fun getItemLive(personID: Int): LiveData<PersonItem>

    fun getItemListLive(): LiveData<List<PersonItem>>

    fun getItemListLiveByMedicineID(medicineID: Int): LiveData<List<PersonItem>>

    fun getMainPersonIDLive(): LiveData<Int>
}