package com.example.medihelper.localdatabase.repositories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medihelper.localdatabase.entities.PersonEntity
import com.example.medihelper.localdatabase.pojos.PersonItem
import com.example.medihelper.localdatabase.pojos.PersonOptionsData

interface PersonRepository : ServerSyncRepository<PersonEntity> {

    suspend fun insert(personEntity: PersonEntity): Int

    suspend fun update(personEntity: PersonEntity)

    suspend fun delete(personID: Int)

    suspend fun deleteAllWithMain()

    suspend fun getEntity(personID: Int): PersonEntity

    suspend fun getEntityList(): List<PersonEntity>

    suspend fun getItem(personID: Int): PersonItem

    suspend fun getMainPersonID(): Int?

    suspend fun getMainPersonEntity(): PersonEntity

    fun getItemLive(personID: Int): LiveData<PersonItem>

    fun getOptionsDataLive(personID: Int): LiveData<PersonOptionsData>

    fun getItemListLive(): LiveData<List<PersonItem>>

    fun getItemListLiveByMedicineID(medicineID: Int): LiveData<List<PersonItem>>

    fun getMainPersonIDLive(): LiveData<Int>

    fun getMainPersonItemLive(): LiveData<PersonItem>
}